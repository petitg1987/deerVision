ALTER TABLE action_completion_time ADD creation_date_time timestamp without time zone;
UPDATE action_completion_time SET creation_date_time = '2023-05-26 00:00:00'::timestamp;