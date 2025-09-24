#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "1. Starting to build the backend image"
cd ../../deer-vision-backend/
mvn -B clean package
mkdir -p /tmp/img_backend
rm -rf /tmp/img_backend/*
rm -rf /tmp/img_backend/.*
cp ./target/deer-vision*.jar /tmp/img_backend/deer-vision-backend.jar
cp ../setup/ci_cd/backend/Dockerfile /tmp/img_backend/Dockerfile
docker build -t deer-vision-backend:latest /tmp/img_backend

echo "2. Get backend deployment information"
old_container_name=deer-vision-backend-blue
old_port='13002'
new_container_name=deer-vision-backend-green
new_port='13003'
greenContainerExist=$(docker ps --format '{{.Names}}' | grep -q "deer-vision-backend-green") || true
if [[ -n "$greenContainerExist" ]]; then
  old_container_name=deer-vision-backend-green
  old_port='13003'
  new_container_name=${DOCKER_BASE_CONTAINER_NAME}Blue
  new_port='13002'
fi
echo "  - Old container name: ${old_container_name}:${old_port}, New container name: ${new_container_name}:${new_port}"

echo "3. Loading backend secret parameters"
dbPassword=$(cat /data/ci_cd/secret/deerVisionDbPassword)
adminPassword=$(cat /data/ci_cd/secret/deerVisionAdminPassword)
adminJwtToken=$(cat /data/ci_cd/secret/deerVisionAdminJwtSecret)

echo "4. Deploy the new Docker image (${new_container_name}:${new_port})"
docker network create "deer-vision-network" || true
docker network connect "deer-vision-network" "deer-vision-db" || true
docker stop "$new_container_name" || true #useless except when script crashed
docker rm "$new_container_name" || true #useless except when script crashed
docker run -d \
    -p ${new_port}:8080 \
    -e DB_PASSWORD="$dbPassword" \
    -e ADMIN_PASSWORD="$adminPassword" \
    -e ADMIN_JWT_SECRET="$adminJwtToken" \
    --restart always \
    --name "${new_container_name}" \
    --network="deer-vision-network" \
    deer-vision-backend:latest


#docker stop deer-vision-backend || true
#docker rm deer-vision-backend || true