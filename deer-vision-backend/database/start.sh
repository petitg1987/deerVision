#!/bin/bash

set -e
cd "$(dirname "$0")"

docker stop deervision-db || true
docker rm deervision-db || true
docker run --name deervision-db -e POSTGRES_PASSWORD=dev -v /home/greg/project/deerVision/deer-vision-backend/database/db-data/:/var/lib/postgresql/data -p 5432:5432 -d postgres:14.5

sleep 5 #Wait volume is created
sudo chmod 755 /home/greg/project/deerVision/deer-vision-backend/database/db-data/
