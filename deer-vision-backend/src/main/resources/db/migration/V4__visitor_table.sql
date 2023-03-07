CREATE TABLE visitor (
  id character varying(41) PRIMARY KEY,
  visit_date date,
  country_code character varying(50)
);

CREATE INDEX index_visitdate ON visitor USING btree (visit_date);
