#!/bin/bash

set -e
cd "$(dirname "$0")"
export AWS_DEFAULT_PROFILE=deervision

APP_NAME='deervision'
AWS_ACCOUNT_ID='496124100072'
DOCKER_REGISTRY_NAME='deervision'
DOCKER_IMAGE_NAME='deervision'

aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com

latest_tag=$(aws ecr list-images --repository-name $DOCKER_REGISTRY_NAME --filter tagStatus=TAGGED --query 'imageIds | max_by(@, &imagePushedAt) | imageTag' --output text)


docker pull $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/$DOCKER_REGISTRY_NAME:$DOCKER_IMAGE_NAME

docker stop $DOCKER_IMAGE_NAME
docker rm $DOCKER_IMAGE_NAME

docker run -d -p 9090:8080 --restart always --name $DOCKER_IMAGE_NAME $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/$DOCKER_REGISTRY_NAME:$DOCKER_IMAGE_NAME
