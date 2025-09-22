#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "Starting to build the image"

cd ../../deer-vision-backend/
mvn -B clean package

mkdir -p /working
rm -rf /working/*
rm -rf /working.*

cp ./target/deer-vision*.jar /working/deer-vision.jar
cp ../setup/ci_cd/backend/Dockerfile /working/Dockerfile
cd /working/
docker build -t deer-vision:latest .
