#!/bin/bash

set -e
set +x
cd "$(dirname "$0")"

AWS_ACCOUNT_ID='496124100072'
AWS_REGION='eu-central-1'
APP_NAME='deervision'
DOCKER_REGISTRY_NAME='deervision'
DOCKER_IMAGE_NAME='deervision'
DOCKER_NETWORK='app-network'

aws ecr get-login-password --region $AWS_REGION | sudo docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
image_info=$(aws ecr describe-images --region $AWS_REGION --repository-name $DOCKER_REGISTRY_NAME --query 'imageDetails[].[imageTags[0], imagePushedAt]' --output text)
latest_tag=$(echo "$image_info" | sort -k2 -r | awk '{print $1}' | head -n 1)

sudo docker pull $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/$DOCKER_REGISTRY_NAME:$latest_tag
sudo docker stop $DOCKER_IMAGE_NAME || true
sudo docker rm $DOCKER_IMAGE_NAME || true
sudo docker network create $DOCKER_NETWORK || true
sudo docker network connect $DOCKER_NETWORK $APP_NAME-db || true
sudo docker run -d -p 8081:8080 --restart always --name $DOCKER_IMAGE_NAME --network=$DOCKER_NETWORK $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$DOCKER_REGISTRY_NAME:$latest_tag
sudo docker image prune -a -f
