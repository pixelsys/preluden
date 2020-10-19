package com.ef.listener;

import com.ef.model.LogRecord;

/**
 * ParseListener instances get notified when a log record is parsed, having access to the log record in a pub/sub fashion
 * Basic lifecycle events are supported through the start and stop method. Before one parse execution start will be
 * invoked and stop afterwards.
 */
public interface ParseListener {

    default void start() {
        System.out.println(getClass() + " started.");
    }

    void receive(LogRecord log);

    default void stop() {
        System.out.println(getClass() + " finished.");
    }

}
