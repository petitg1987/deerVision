#!/bin/bash

set -e

echo "Starting to build the backend image"
cd "$(dirname "$0")"
cd ../../deer-vision-backend/
mvn -B clean package
mkdir -p /img_backend
rm -rf /img_backend/*
rm -rf /img_backend/.*
cp ./target/deer-vision*.jar /img_backend/deer-vision.jar
cp ../setup/ci_cd/backend/Dockerfile /img_backend/Dockerfile
docker build -t deer-vision:latest /img_backend

docker stop ntfy