CREATE TABLE usage (
  id SERIAL PRIMARY KEY,
  app_id character varying(255),
  app_version character varying(255),
  date_time timestamp without time zone,
  operating_system integer,
  request_key character varying(255)
);

CREATE INDEX index_datetime ON usage USING btree (date_time);
