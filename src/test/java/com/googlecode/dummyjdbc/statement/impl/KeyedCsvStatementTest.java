package com.googlecode.dummyjdbc.statement.impl;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.dummyjdbc.DummyJdbcDriver;

public final class KeyedCsvStatementTest {

	private ResultSet resultSet;

	@Before
	public void setup() throws ClassNotFoundException, SQLException, URISyntaxException {
		Class.forName(DummyJdbcDriver.class.getCanonicalName());

		Properties props = new Properties();
		Connection connection = DriverManager.getConnection("jdbc:any:keyed", props);
		Statement statement = connection.createStatement();

		Assert.assertTrue(statement instanceof KeyedCsvStatement);
		resultSet = statement
				.executeQuery("SELECT count(*) FROM test_table -- SQL_SAMPLE_RESULT_DATA_FILE=test_table.csv \nSELECT * FROM test_table");
		resultSet = statement
				.executeQuery("SELECT count(*) FROM test_table -- SQL_SAMPLE_RESULT_DATA_FILE=test_table.csv");
	}

	@Test
	public void testGetByColumnName() throws SQLException {

		Assert.assertTrue(resultSet.next());

		Assert.assertEquals(1, resultSet.getInt("id"));
		Assert.assertEquals("Germany", resultSet.getString("country_name"));
		Assert.assertEquals("DE", resultSet.getString("country_iso"));
	}

	@Test
	public void testGetByColumnIndex() throws SQLException {

		Assert.assertTrue(resultSet.next());

		Assert.assertEquals(1, resultSet.getInt(1));
		Assert.assertEquals("Germany", resultSet.getString(2));
		Assert.assertEquals("DE", resultSet.getString(3));
	}

	@Test(expected = SQLException.class)
	public void testGetInvalidColumnName() throws SQLException {

		Assert.assertTrue(resultSet.next());

		resultSet.getInt("undefined");

		Assert.fail("Expected exception not thrown");
	}

	@Test(expected = SQLException.class)
	public void testGetInvalidColumnindex() throws SQLException {

		Assert.assertTrue(resultSet.next());
		resultSet.getInt(17);

		Assert.fail("Expected exception not thrown");
	}
}
