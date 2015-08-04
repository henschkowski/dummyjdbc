package com.googlecode.dummyjdbc.connection.impl;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.googlecode.dummyjdbc.connection.ConnectionAdapter;
import com.googlecode.dummyjdbc.statement.impl.KeyedCsvPreparedStatement;
import com.googlecode.dummyjdbc.statement.impl.KeyedCsvStatement;

/**
 * Connection which implements the methods {@link #createStatement()} and
 * {@link #prepareStatement(String)}. The {@link KeyedDummyConnection} tries to
 * open a CSV file in the directory <code>./table/</code> with the name
 * <code>&lt;tablename&gt;.csv</code> and returns the contained values. The
 * query result will contain all values from the file, it will not be narrowed
 * by the query. The first line of the CSV file has to contain the column names.
 * 
 * @author Kai Winter
 */
public class KeyedDummyConnection extends ConnectionAdapter {

	private Map<String, File> resultsFilenameCache;
	private String path = null;

	public String getPath() {
		return path;
	}

	public KeyedDummyConnection(Map<String, File> tableResources) {
		this.resultsFilenameCache = tableResources;
	}

	public KeyedDummyConnection(Map<String, File> tableResources, String path) {
		this.resultsFilenameCache = tableResources;
		this.path = path;
	}

	@Override
	public Statement createStatement() throws SQLException {
		return new KeyedCsvStatement(resultsFilenameCache, getPath());
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new KeyedCsvPreparedStatement(resultsFilenameCache, sql, getPath());
	};
}
