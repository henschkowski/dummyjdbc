# Building using Maven #
Beginning with dummyjdbc-1.2 Java 7 is used for the build. In Java 7 new methods were introduced in `java.sql` which made it necessary to extend dummyjdbc.

To create a jar which only contains the DummyJDBC classes call:
```
mvn package
```

To create a jar which additionally contains the dependencies (OpenCSV, AspectJ, logback, slf4j) call
```
mvn package assembly:single
```

# Build Problems #
## Wrong JDK version (dummyjdbc < 1.2) ##
DummyJDBC extends classes from the `java.sql` package. In some classes new methods were introduced in Java 7. This only applies for dummyjdbc before 1.2. Beginning with dummyjdbc-1.2 Java 7 is used for the build.

### Problem ###
You get the following output from Maven:
```
...
[INFO] -------------------------------------------------------------
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/statement/PreparedStatementAdapter.java:[31,8] com.googlecode.dummyjdbc.statement.PreparedStatementAdapter is not abstract and does not override abstract method isCloseOnCompletion() in java.sql.Statement
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/statement/StatementAdapter.java:[14,8] com.googlecode.dummyjdbc.statement.StatementAdapter is not abstract and does not override abstract method isCloseOnCompletion() in java.sql.Statement
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/resultset/DummyResultSet.java:[30,8] com.googlecode.dummyjdbc.resultset.DummyResultSet is not abstract and does not override abstract method <T>getObject(java.lang.String,java.lang.Class<T>) in java.sql.ResultSet
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/connection/ConnectionAdapter.java:[26,8] com.googlecode.dummyjdbc.connection.ConnectionAdapter is not abstract and does not override abstract method getNetworkTimeout() in java.sql.Connection
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/DummyJdbcDriver.java:[21,14] com.googlecode.dummyjdbc.DummyJdbcDriver is not abstract and does not override abstract method getParentLogger() in java.sql.Driver
[INFO] 5 errors 
[INFO] -------------------------------------------------------------
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 9.105s
[INFO] Finished at: Fri Jun 28 10:23:38 CEST 2013
[INFO] Final Memory: 10M/67M
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.1:compile (default-compile) on project dummyjdbc: Compilation failure: Compilation failure:
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/statement/PreparedStatementAdapter.java:[31,8] com.googlecode.dummyjdbc.statement.PreparedStatementAdapter is not abstract and does not override abstract method isCloseOnCompletion() in java.sql.Statement
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/statement/StatementAdapter.java:[14,8] com.googlecode.dummyjdbc.statement.StatementAdapter is not abstract and does not override abstract method isCloseOnCompletion() in java.sql.Statement
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/resultset/DummyResultSet.java:[30,8] com.googlecode.dummyjdbc.resultset.DummyResultSet is not abstract and does not override abstract method <T>getObject(java.lang.String,java.lang.Class<T>) in java.sql.ResultSet
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/connection/ConnectionAdapter.java:[26,8] com.googlecode.dummyjdbc.connection.ConnectionAdapter is not abstract and does not override abstract method getNetworkTimeout() in java.sql.Connection
[ERROR] /C:/e4/DummyJdbc/src/main/java/com/googlecode/dummyjdbc/DummyJdbcDriver.java:[21,14] com.googlecode.dummyjdbc.DummyJdbcDriver is not abstract and does not override abstract method getParentLogger() in java.sql.Driver
[ERROR] -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
```

### Solution ###
Use Java 6 to build DummyJDBC.