package com.googlecode.dummyjdbc.statement.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.googlecode.dummyjdbc.resultset.DummyResultSet;
import com.googlecode.dummyjdbc.resultset.impl.CSVResultSet;
import com.googlecode.dummyjdbc.statement.StatementAdapter;

/**
 * This class does the actual work of the Generic... classes. It tries to open a CSV file for  the
 * query (query need to have a line with SQL_SAMPLE_RESULT_DATA_FILE=<filename.csv> in a comment line)
 * and parses the contained data.
 * 
 * @author Kai Winter
 * @author Ralf Henschkowski
 */
public final class KeyedCsvStatement extends StatementAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KeyedCsvStatement.class);

	/** Pattern to get table name from an SQL statement. */
	private static final Pattern STMTRESULTS_PATTERN = Pattern.compile(".*--\\s*SQL_SAMPLE_RESULT_DATA_FILE=(\\S*)\\s.*", Pattern.CASE_INSENSITIVE);

	private final Map<String, File> resultResources;

	/**
	 * Constructs a new {@link KeyedCsvStatement}.
	 * 
	 * @param resultResources
	 *            {@link Map} of table name to CSV file.
	 */
	public KeyedCsvStatement(Map<String, File> tableResources) {
		this.resultResources = tableResources;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {

		// Try to find the results data file in the statement (comment)
		Matcher stmtResultsMatcher = STMTRESULTS_PATTERN.matcher(sql);
		if (stmtResultsMatcher.matches()) {
			String stmtResultsFileName = stmtResultsMatcher.group(1);
			return createResultSet(stmtResultsFileName);
		}

		return new DummyResultSet();
	}

	private ResultSet createResultSet(String resultsFile) {
		File resource = resultResources.get(resultsFile.toLowerCase());
		if (resource == null) {
			// Try to load a file from the ./result_data/ directory
			
			try {
				
//				Enumeration<URL> e = getClass().getClassLoader().getResources("");
//		        while (e.hasMoreElements())
//		        {
//		            System.out.println("ClassLoader Resource: " + e.nextElement());
//		        }
//		        System.out.println("Class Resource: " + Test.class.getResource("/"));
//				
				URL url = getClass().getClassLoader().getResource("result_data/" + resultsFile);
				
				if (url == null) {
					LOGGER.info("No result data definition file found for '{}', using DummyResultSet.", resultsFile);
					return new DummyResultSet();
				} else {
					resource = new File(url.toURI());
				}
			} catch (Exception e) {
				LOGGER.error("Error creating URI for table file: {}", e.getMessage(), e);
			}
		}

		FileInputStream dummyDataStream = null;
		try {
			dummyDataStream = new FileInputStream(resource);
			return createGenericResultSet(resultsFile, dummyDataStream);
		} catch (FileNotFoundException e) {
			LOGGER.info("No results data definition found for '{}', using DummyResultSet.", resultsFile);
		} finally {
			if (dummyDataStream != null) {
				try {
					dummyDataStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return new DummyResultSet();
	}

	private ResultSet createGenericResultSet(String resultsName, InputStream dummyResultsDataStream) {

		// Maps columns to a number of available values.
		Collection<LinkedHashMap<String, String>> entries = new ArrayList<LinkedHashMap<String, String>>();

		CSVReader dummyResultsReader = null;
		try {

			dummyResultsReader = new CSVReader(new InputStreamReader(dummyResultsDataStream));

			// Read header
			String[] header = dummyResultsReader.readNext();
			if (header != null) {

				String[] data;
				// Read data
				while ((data = dummyResultsReader.readNext()) != null) {
					if (header.length == data.length) {
						LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
						for (int i = 0; i < header.length; i++) {
							if (map.containsKey(header[i].trim().toUpperCase())) {
								String message = MessageFormat.format("Duplicate column in file ''{0}.txt: {1}",
										resultsName, header[i]);
								throw new IllegalArgumentException(message);
							}
							map.put(header[i].trim().toUpperCase(), data[i].trim());

						}
						entries.add(map);
					} else {
						throw new IllegalArgumentException("Length of data does not fit header length.");
					}

				}
			}
			return new CSVResultSet(resultsName, entries);

		} catch (IOException e) {
			LOGGER.error("Error while reading data from CSV", e);
		} finally {
			if (dummyResultsReader != null) {
				try {
					dummyResultsReader.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return new DummyResultSet();
	}
}
