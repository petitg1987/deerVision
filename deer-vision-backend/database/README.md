# Manage local database
* Start dev. database: `./database/start.sh`
* Stop dev. database: `./database/stop.sh`
 
# Access to local database
## Pre-requisites
* Install Postgres tools: `sudo apt install postgresql-client-common postgresql-client-16`

## Console
* Execute (password: dev): `psql -h localhost -d postgres -p 5432 -U postgres`
* Commands:
  * List databases: \l+
  * List tables: \dt public.* 
  * Execute select, update, delete queries (must end with semi-colon) 
  * Exit: \q

## Access to docker container
* Execute `sudo docker exec -it deervision-db bash`
