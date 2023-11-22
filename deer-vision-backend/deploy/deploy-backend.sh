#!/bin/bash

set -e
cd "$(dirname "$0")"

APP_NAME='deervision'
AWS_ACCOUNT_ID='496124100072'
DOCKER_REGISTRY_NAME='deervision'
DOCKER_IMAGE_NAME='deervision'

aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com

image_info=$(aws ecr describe-images --repository-name $DOCKER_REGISTRY_NAME --query 'imageDetails[].[imageTags[0], imagePushedAt]' --output text)
latest_tag=$(echo "$image_info" | sort -k2 -r | awk '{print $1}' | head -n 1)

docker pull $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/$DOCKER_REGISTRY_NAME:$latest_tag

docker stop $DOCKER_IMAGE_NAME
docker rm $DOCKER_IMAGE_NAME

docker run -d -p 9090:8080 --restart always --name $DOCKER_IMAGE_NAME $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/$DOCKER_REGISTRY_NAME:$DOCKER_IMAGE_NAME
