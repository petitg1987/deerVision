#!/bin/bash

set -e
cd "$(dirname "$0")"
directory_dir=$1

echo "1. Starting to build the frontend image"
cd "$directory_dir"/deer-vision-frontend/
yarn install
yarn build

#mkdir -p /tmp/img_backend
#rm -rf /tmp/img_backend/*
#rm -rf /tmp/img_backend/.*
#cp ./target/deer-vision*.jar /tmp/img_backend/deer-vision-backend.jar
#cp ../setup/ci_cd/backend/Dockerfile /tmp/img_backend/Dockerfile
#docker build -t deer-vision-backend:latest /tmp/img_backend



echo "xxxx. Cleaning local Docker images"
docker image prune -a -f
docker system prune -a -f
