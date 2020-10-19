package com.ef;

import com.ef.listener.ParseListener;
import com.ef.model.LogRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class LogParser {

    public static final String DELIMITER = "|";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final ParseListener[] listeners;

    public LogParser(ParseListener[] listeners) {
        this.listeners = listeners;
    }

    static LogRecord parse(String line) {
        String[] chunks = line.split(Pattern.quote(DELIMITER));
        if(5 != chunks.length)
            throw new IllegalArgumentException("Invalid line, should contain 5 elements: " + line);
        // strip quotes
        for(int i = 0; i < 5; ++i) {
            if(chunks[i].charAt(0) == '"') {
                chunks[i] = chunks[i].substring(1, chunks[i].length() - 1);
            }
        }
        return new LogRecord(LocalDateTime.parse(chunks[0], DATE_FORMAT), chunks[1], chunks[2], Integer.valueOf(chunks[3]), chunks[4]);
    }

    public void parse(File logFile) {
        try (BufferedReader reader = Files.newBufferedReader(logFile.toPath())) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                LogRecord log = parse(line);
                for(ParseListener listener : listeners)
                    listener.receive(log);
            }
        } catch (IOException ioe) {
            System.err.format("Error while reading the log file: %s%n", ioe);
        }
    }

}
