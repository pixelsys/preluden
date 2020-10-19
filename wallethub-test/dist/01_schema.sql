CREATE TABLE log_record (
    log_date        TIMESTAMP,
    ip              VARCHAR(15),
    request         VARCHAR(2000),
    status          SMALLINT,
    user_agent      VARCHAR(2000)
);

CREATE INDEX log_record_lookup_idx ON log_record (log_date);
CREATE INDEX log_record_lookup2_idx ON log_record (ip);

CREATE TABLE load_summary (
    load_time       TIMESTAMP,
    result          TEXT
);
