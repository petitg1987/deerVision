# Manage local database
## Pre-requisites
* Install Postgres tools: `sudo apt install postgresql-client-common postgresql-client-16`

## Start or stop
* Start dev. database: `./database/start.sh`
* Stop dev. database: `./database/stop.sh`

## Console
* Execute (password: dev): `psql -h localhost -d postgres -p 5432 -U postgres`
* Commands:
  * List databases: \l+
  * List tables: \dt public.* 
  * Execute select, update, delete queries (must end with semi-colon) 
  * Exit: \q

# Manager remote database
## Access to docker container
* Execute `sudo docker exec -it deer-vision-db bash`
