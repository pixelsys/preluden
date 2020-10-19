package com.ef;

import com.ef.listener.DatabaseLoadListener;
import com.ef.listener.ParseListener;
import com.ef.listener.StatsListener;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.LocalDateTimeOptionHandler;
import org.postgresql.Driver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.kohsuke.args4j.OptionHandlerFilter.ALL;

public class Parser {

    @Option(name="--accesslog", required = true)
    private String accessLog;

    @Option(name="--startDate", required = true, handler = LocalDateTimeOptionHandler.class)
    private LocalDateTime startDate;

    @Option(name="--duration", required = true)
    private String duration;

    @Option(name="--threshold", required = true)
    private int threshold;

    private static DataSource createDataSource() {
        Driver dbDriver = new Driver();
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(dbDriver, "jdbc:postgresql://localhost/ef_parser", "pixel", "");
        return dataSource;
    }

    public static void main(String[] args) throws IOException {
        new Parser().doMain(args);
    }

    public void doMain(String[] args) throws IOException {
        CmdLineParser cmdLineParser = new CmdLineParser(this);
        Duration d = null;
        File logFile = null;
        try {
            cmdLineParser.parseArgument(args);
            if("daily".equalsIgnoreCase(duration)) {
                d = Duration.of(1, ChronoUnit.DAYS);
            } else if("hourly".equalsIgnoreCase(duration)) {
                d = Duration.of(1, ChronoUnit.HOURS);
            } else {
                throw new CmdLineException("Invalid value duration, needs to be [daily|hourly]");
            }
            logFile = new File(accessLog);
            if(!logFile.isFile()) {
                throw new CmdLineException("Invalid file: " + accessLog);
            }
        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println("java Parser [options...] arguments...");
            cmdLineParser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java Parser " + cmdLineParser.printExample(ALL));
            return;
        }
        final LocalDateTime endDate = startDate.plus(d);
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(createDataSource());

        // initialise parse listeners and execute the main logic
        ParseListener[] listeners = new ParseListener[]{
                new StatsListener(startDate, endDate, threshold, jdbcTemplate),
                new DatabaseLoadListener(jdbcTemplate)
        };

        for(ParseListener listener : listeners) {
            listener.start();
        }

        LogParser parser = new LogParser(listeners);
        parser.parse(logFile);

        for(ParseListener listener : listeners) {
            listener.stop();
        }
    }

}
