package com.googlecode.dummyjdbc.statement.impl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.googlecode.dummyjdbc.statement.PreparedStatementAdapter;

/**
 * Wraps the {@link CsvStatement} as a prepared statement.
 * 
 * @author Kai Winter
 */
public class KeyedCsvPreparedStatement extends PreparedStatementAdapter {

	private final KeyedCsvStatement statement;
	private final String sql;

	/**
	 * Constructs a new {@link KeyedCsvPreparedStatement}.
	 * 
	 * @param tableResources
	 *            {@link Map} of table name to CSV file.
	 * @param sql
	 *            the SQL statement.
	 */
	public KeyedCsvPreparedStatement(Map<String, File> tableResources, String sql) {
		this.statement = new KeyedCsvStatement(tableResources);
		this.sql = sql;
	}

	public KeyedCsvPreparedStatement(Map<String, File> tableResources, String sql, String path) {
		this.statement = new KeyedCsvStatement(tableResources, path);
		this.sql = sql;

	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return statement.executeQuery(sql);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		return statement.executeQuery(sql);
	}

}
