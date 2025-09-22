#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "Starting to build the image"

cd ../../deer-vision-backend/
mvn clean install
