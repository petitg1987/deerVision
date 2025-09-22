#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "Starting to build the image"

cd ../../deer-vision-backend/
mvn -B clean package

cd ./deploy/
cp ../target/deer-vision*.jar ./deer-vision.jar
docker build -t deer-vision:latest .
