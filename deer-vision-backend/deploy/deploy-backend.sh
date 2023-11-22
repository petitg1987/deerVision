#!/bin/bash

set -e
set +x
cd "$(dirname "$0")"

APP_NAME='deervision'
AWS_ACCOUNT_ID='496124100072'
AWS_REGION='eu-central-1'
DOCKER_REGISTRY_NAME='deervision'
DOCKER_IMAGE_NAME='deervision'

aws ecr get-login-password --region $AWS_REGION | sudo docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
image_info=$(aws ecr describe-images --region $AWS_REGION --repository-name $DOCKER_REGISTRY_NAME --query 'imageDetails[].[imageTags[0], imagePushedAt]' --output text)
latest_tag=$(echo "$image_info" | sort -k2 -r | awk '{print $1}' | head -n 1)
sudo docker pull $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/$DOCKER_REGISTRY_NAME:$latest_tag

sudo docker stop $DOCKER_IMAGE_NAME || true
sudo docker rm $DOCKER_IMAGE_NAME || true

#sudo docker run -d -p 5001:5000 --restart always --name $DOCKER_IMAGE_NAME $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$DOCKER_REGISTRY_NAME:$latest_tag

sudo docker run -d -p 5001:8080 --name $DOCKER_IMAGE_NAME $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$DOCKER_REGISTRY_NAME:$latest_tag