# dummyjdbc [![Build Status](https://drone.io/github.com/kaiwinter/dummyjdbc/status.png)](https://drone.io/github.com/kaiwinter/dummyjdbc/latest)

dummyjdbc answers database requests of any application with dummy data to be independent of an existing database.

The library can either return dummy values, or values defined by you in a CSV file. The files are determined by the SQL query which makes this a very flexible tool. Also results of Stored Procedures can be mocked with data from CSV files.

For more details please see the [Wiki](https://github.com/kaiwinter/dummyjdbc/wiki)

The original dummyjdbc was extended so that it accepts the filename of the results CSV file as a comment in the statement SQL. The benefit beeing that also arbitrary complex querys can be used.

To use the "keyed" driver, instantiate the driver with property "mode" set to "keyed" (the default is the table-based driver):
```
Properties props = new Properties();
props.setProperty("mode", "keyed");
Connection connection = DriverManager.getConnection("any", props);
```
		

Then, in the SQL sent to the connection, place a comment as follows:

```

-- SQL_SAMPLE_RESULT_DATA_FILE=test_table.csv
   
```

The  ```test_table.csv``` must be placed into a directory ```result_data``` in the classpath (into ```src/test/resources/result_data/``` for a Maven project so that it is placed in ```target/test-classes/result_data``` when the tests are run.

   
   
