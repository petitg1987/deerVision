#!/bin/bash

set -e
cd "$(dirname "$0")"
directory_dir=$1

echo "Starting the deployment for repository: ${directory_dir}"

cd "${directory_dir}/setup/ci_cd/backend/"
chmod +x ./deploy.sh
./deploy.sh "$directory_dir"

cd "${directory_dir}/setup/ci_cd/frontend/"
chmod +x ./deploy.sh
./deploy.sh "$directory_dir"
