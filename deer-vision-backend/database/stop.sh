#!/bin/bash

set -e
cd "$(dirname "$0")"

docker stop postgres-db || true
docker rm postgres-db || true
