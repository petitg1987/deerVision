#!/bin/bash

set -e
cd "$(dirname "$0")"

#TODO provide password from ansible variable (use file / volume ?) + add network to access to the database

echo "Starting to deploy the backend image"
docker stop deer-vision-backend || true
docker rm deer-vision-backend || true

sudo docker network create "app-dv-network" || true
sudo docker network connect "app-dv-network" "deer-vision-db" || true

docker run -d \
    -p 13002:8080 \
    -e DB_PASSWORD="$DB_PASSWORD" \
    -e ADMIN_PASSWORD="$ADMIN_PASSWORD" \
    -e ADMIN_JWT_SECRET="$ADMIN_JWT_SECRET" \
    --restart always \
    --name deer-vision-backend \
    --network="app-dv-network"
    deer-vision-backend:latest
