#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "Starting to deploy the backend image"
docker stop deer-vision-backend || true
docker rm deer-vision-backend || true

docker network create "deer-vision-network" || true
docker network connect "deer-vision-network" "deer-vision-db" || true

dbPassword=$(cat /data/secret/deerVisionDbPassword)
adminPassword=$(cat /data/secret/deerVisionAdminPassword)
adminJwtToken=$(cat /data/secret/deerVisionAdminJwtSecret)

docker run -d \
    -p 13002:8080 \
    -e DB_PASSWORD="$dbPassword" \
    -e ADMIN_PASSWORD="$adminPassword" \
    -e ADMIN_JWT_SECRET="$adminJwtToken" \
    --restart always \
    --name deer-vision-backend \
    --network="deer-vision-network" \
    deer-vision-backend:latest
