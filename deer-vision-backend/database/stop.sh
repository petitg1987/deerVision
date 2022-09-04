#!/bin/bash

set -e
cd "$(dirname "$0")"

docker stop deervision-db || true
docker rm deervision-db || true
