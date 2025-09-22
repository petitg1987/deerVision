#!/bin/bash

set -e
cd "$(dirname "$0")"

echo "Starting to build the image"

apt update
apt install -y maven openjdk-21-jdk
cd ../../deer-vision-backend/
maven clean install
