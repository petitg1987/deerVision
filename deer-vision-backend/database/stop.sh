#!/bin/bash

set -e
cd "$(dirname "$0")"

docker stop deer-vision-db || true
docker rm deer-vision-db || true
