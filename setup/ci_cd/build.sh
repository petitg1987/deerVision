#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "Starting to build the backend image"
cd ../../deer-vision-backend/
mvn -B clean package
mkdir -p /tmp/img_backend
rm -rf /tmp/img_backend/*
rm -rf /tmp/img_backend/.*
cp ./target/deer-vision*.jar /tmp/img_backend/deer-vision-backend.jar
cp ../setup/ci_cd/backend/Dockerfile /tmp/img_backend/Dockerfile
docker build -t deer-vision-backend:latest /tmp/img_backend
