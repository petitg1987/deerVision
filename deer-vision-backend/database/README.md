# Manage database
* Start dev. database: `./start.sh`
* Stop dev. database: `./stop.sh`
 
# Access to database
## Pre-requisites
* Install Postgres tools: `sudo apt install postgresql-client-common postgresql-client-14`

## Console
* Execute (password: dev): `psql -h localhost -d postgres -p 5432 -U postgres`
* Commands:
  * List databases: \l+
  * List tables: \dt public.* 
  * Execute select, update, delete queries (must end with semi-colon) 
  * Exit: \q

## Access to docker container
* Execute `docker exec -it postgres-db bash`

## Create database dump
* Execute "docker exec -t postgres-db pg_dump -U postgres > ~/postgres_dump_`date +%d-%m-%Y"_"%H_%M_%S`.sql"
