WalletHub Parser Tech Exercise
------------------------------

Solution by Daniel Petyus (petyus.daniel@gmail.com), submitted on 08/05/2018, version 1.0.0

This application uses a few libraries (defined in Maven pom.xml) to easily fulfill a common tasks, such as
  - jUnit for testing
  - args4j for command line handling
  - Spring JDBC for database operations

Note: the application uses PostgreSQL instead of MySQL.

There are two main components of the application

1)  LogParser which is a simple text file parser (like CSV), in this case an arbitrary log file parser emitting
    LogRecord objects for each log rows parsed.

2)  These LogRecord objects are received by ParseListeners. For this exercise two listeneres were created:
    a) DatabaseLoadListener which clears the log_record table before each run and then putting all the log rows in the
       database
    b) StatsListener which gathers statistics about the logs and delivers the requires business logic: filtering for
       start, end date and threshold and then storing the results in the summary datatable.

Deliverables
------------

(1) Please use the provided .jar or create one with `mvn package` and then run the application with specified arguments

(2) in the `src` directory

(3) `dist/01_schema.sql` file

(4) SQL queries:

SELECT ip
  FROM log_record
  WHERE log_date > '2017-01-01 13:00:00' AND log_date < '2017-01-01 14:00:00'
  GROUP by ip
    HAVING COUNT(log_date) >= 100;

SELECT *
  FROM log_record
  WHERE ip='192.168.125.121'
