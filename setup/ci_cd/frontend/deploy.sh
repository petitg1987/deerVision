#!/bin/bash

set -e
cd "$(dirname "$0")"
directory_dir=$1

echo " "
echo "-----------------------------------------------------"
echo "1. Starting to build the frontend image"
echo "-----------------------------------------------------"
cd "$directory_dir"/deer-vision-frontend/
yarn install
yarn build
rm -rf /tmp/img_frontend/
mkdir -p /tmp/img_frontend/
cp -r ./build/ /tmp/img_frontend/
cp ../setup/ci_cd/frontend/Dockerfile /tmp/img_frontend/Dockerfile
docker build -t deer-vision-frontend:latest /tmp/img_frontend

echo " "
echo "-----------------------------------------------------"
echo "2. Deploy the new Docker image (deer-vision-frontend:latest)"
echo "-----------------------------------------------------"
docker stop deer-vision-frontend || true
docker rm deer-vision-frontend || true
docker run -d \
    -p 13005:80 \
    --restart always \
    --name deer-vision-frontend \
    deer-vision-frontend:latest

echo " "
echo "-----------------------------------------------------"
echo "3. Cleaning local Docker images"
echo "-----------------------------------------------------"
docker image prune -a -f
docker system prune -a -f
