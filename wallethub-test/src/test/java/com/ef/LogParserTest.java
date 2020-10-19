package com.ef;

import com.ef.model.LogRecord;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class LogParserTest {

    @Test
    public void testParseRecord() {
        final String log = "2017-01-01 00:01:11.292|192.168.118.220|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1\"";
        LogRecord expected = new LogRecord(
                LocalDateTime.of(2017, 1, 1, 0, 1, 11, 292000000),
                "192.168.118.220",
                "GET / HTTP/1.1",
                200,
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        LogRecord actual = LogParser.parse(log);
        assertEquals(expected, actual);
    }


}
