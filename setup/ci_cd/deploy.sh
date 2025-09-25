#!/bin/bash

set -e
cd "$(dirname "$0")"
repository_dir=$1

cd "${repository_dir}/setup/ci_cd/backend/"
chmod +x ./deploy.sh
./deploy.sh "$repository_dir"

cd "${repository_dir}/setup/ci_cd/frontend/"
chmod +x ./deploy.sh
./deploy.sh "$repository_dir"
