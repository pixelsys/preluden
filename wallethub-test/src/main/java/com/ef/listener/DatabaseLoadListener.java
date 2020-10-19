package com.ef.listener;

import com.ef.model.LogRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * This @ParseListener loads all the log records to the database.
 */
public class DatabaseLoadListener implements ParseListener {

    private static final int BATCH_SIZE = 1000;

    private final JdbcTemplate jdbcTemplate;
    private final List<LogRecord> logs;

    public DatabaseLoadListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logs = new LinkedList<>();
    }

    @Override
    public void start() {
        jdbcTemplate.execute("TRUNCATE TABLE log_record");
    }

    @Override
    public void receive(LogRecord log) {
        if(BATCH_SIZE == logs.size()) {
            batchUpdate();
        } else {
            logs.add(log);
        }
    }

    private void batchUpdate() {
        final String sql = "INSERT INTO log_record (log_date, ip, request, status, user_agent) VALUES (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, logs, BATCH_SIZE, new ParameterizedPreparedStatementSetter<LogRecord>() {

            @Override
            public void setValues(PreparedStatement preparedStatement, LogRecord logRecord) throws SQLException {
                preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(logRecord.getDate()));
                preparedStatement.setString(2, logRecord.getIP());
                preparedStatement.setString(3, logRecord.getRequest());
                preparedStatement.setInt(4, logRecord.getStatus());
                preparedStatement.setString(5, logRecord.getUserAgent());
            }

        });
        logs.clear();
    }

}
