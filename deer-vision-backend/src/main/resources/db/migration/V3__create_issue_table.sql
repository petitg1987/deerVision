CREATE TABLE issue (
  id SERIAL PRIMARY KEY,
  app_id character varying(255),
  app_version character varying(255),
  data oid,
  date_time timestamp without time zone,
  operating_system integer,
  origin integer,
  request_key character varying(255)
);
