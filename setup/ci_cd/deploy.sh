#!/bin/bash

set -e
cd "$(dirname "$0")"

#TODO provide password from ansible variable (use file / volume ?)

echo "Starting to deploy the backend image"
docker stop deer-vision-backend || true
docker rm deer-vision-backend || true

docker network create "deer-vision-network" || true
docker network connect "deer-vision-network" "deer-vision-db" || true

docker run -d \
    -p 13002:8080 \
    -e DB_PASSWORD="$DEER_VISION_DB_PASSWORD" \
    -e ADMIN_PASSWORD="$DEER_VISION_ADMIN_PASSWORD" \
    -e ADMIN_JWT_SECRET="$DEER_VISION_ADMIN_JWT_SECRET" \
    --restart always \
    --name deer-vision-backend \
    --network="deer-vision-network" \
    deer-vision-backend:latest
