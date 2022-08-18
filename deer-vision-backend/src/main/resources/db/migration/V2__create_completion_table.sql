CREATE TABLE action_completion_time (
   id SERIAL PRIMARY KEY,
   action_name character varying(255),
   app_id character varying(255),
   app_version character varying(255),
   completion_time bigint,
   level_id integer,
   request_key character varying(255)
);

CREATE INDEX index_actionname ON action_completion_time USING btree (action_name);
CREATE INDEX index_levelid ON action_completion_time USING btree (level_id);
