package com.ef.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class LogRecord {

    private final LocalDateTime date;
    private final String IP;
    private final String request;
    private final int status;
    private final String userAgent;

    public LogRecord(LocalDateTime date, String IP, String request, int status, String userAgent) {
        this.date = date;
        this.IP = IP;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getIP() {
        return IP;
    }

    public String getRequest() {
        return request;
    }

    public Integer getStatus() {
        return status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogRecord logRecord = (LogRecord) o;
        return status == logRecord.status &&
                Objects.equals(date, logRecord.date) &&
                Objects.equals(IP, logRecord.IP) &&
                Objects.equals(request, logRecord.request) &&
                Objects.equals(userAgent, logRecord.userAgent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, IP, request, status, userAgent);
    }

    @Override
    public String toString() {
        return "LogRecord{" +
                "date=" + date +
                ", IP='" + IP + '\'' +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
