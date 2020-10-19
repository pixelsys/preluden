package com.ef.listener;

import com.ef.model.LogRecord;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * This @ParseListener listens to the log records and collects statistics for the given interval (between start and
 * end date). Results will be published to the database.
 */
public class StatsListener implements ParseListener {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer threshold;
    private final JdbcTemplate jdbcTemplate;

    private final Map<String, Integer> occurences;
    private boolean endDateReached;

    public StatsListener(LocalDateTime startDate, LocalDateTime endDate, Integer threshold, JdbcTemplate jdbcTemplate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.threshold = threshold;
        this.jdbcTemplate = jdbcTemplate;
        occurences = new HashMap<>();
        endDateReached = false;
    }

    @Override
    public void receive(LogRecord log) {
        if(endDateReached)
            return;
        if(endDate.compareTo(log.getDate()) >= 0) {
            if(startDate.compareTo(log.getDate()) < 0) {
                Integer count = occurences.getOrDefault(log.getIP(), 0);
                occurences.put(log.getIP(), count + 1);
            }
        } else {
            endDateReached = true;
        }
    }

    @Override
    public void stop() {
        String message = String.format("The following IPs were blocked because between %s and %s made more than %d requests: ",
                startDate.toString(), endDate.toString(), threshold);
        for(Map.Entry<String, Integer> e : occurences.entrySet()) {
            if(e.getValue() >= threshold) {
                final String ip = e.getKey();
                System.out.println(ip);
                message += "\n" + ip;
            }
        }
        jdbcTemplate.update("INSERT INTO load_summary (load_time, result) VALUES (CURRENT_TIMESTAMP, ?)", message);
    }

}
