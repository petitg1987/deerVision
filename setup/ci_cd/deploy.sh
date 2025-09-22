#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "Starting to deploy the backend image"
docker stop deer-vision-backend || true
docker rm deer-vision-backend || true
docker run -d \
    -p 13002:8080 \
    -e DB_PASSWORD="$DB_PASSWORD" \
    -e ADMIN_PASSWORD="$ADMIN_PASSWORD" \
    -e ADMIN_JWT_SECRET="$ADMIN_JWT_SECRET" \
    --restart always \
    --name deer-vision-backend \
    deer-vision-backend:latest
