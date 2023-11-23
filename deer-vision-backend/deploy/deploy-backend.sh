#!/bin/bash

set -e

AWS_ACCOUNT_ID='496124100072'
AWS_REGION='eu-central-1'
APP_NAME='deervision'
APP_DOMAIN_NAME='deervision.studio'
DOCKER_REGISTRY_NAME='deervision'
DOCKER_BASE_CONTAINER_NAME='deervision'
DOCKER_NETWORK='app-network'

function checkDeploymentSuccess() {
  base_url=$1
  max_attempts=35
  success=false

  for ((i=1; i<=max_attempts; i++)); do
    http_status=$(curl -s -o /dev/null -w "%{http_code}" $base_url/api/test/deploy)
    echo "Attempt $i - HTTP status code: $http_status"

    if [ $http_status -eq 200 ]; then
      echo "Deployment succeed: HTTP 200 return on $base_url"
      success=true
      break
    fi

    sleep 5
  done

  if [ "$success" = false ]; then
    echo "API did not return 200 after $max_attempts attempts on $base_url"
    exit 1
  fi
}

echo "Log into AWS ECR to get the last image id"
aws ecr get-login-password --region $AWS_REGION | sudo docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
image_info=$(aws ecr describe-images --region $AWS_REGION --repository-name $DOCKER_REGISTRY_NAME --query 'imageDetails[].[imageTags[0], imagePushedAt]' --output text)

echo "Get deployment informations"
old_container_name=${DOCKER_BASE_CONTAINER_NAME}Blue
old_port='8080'
new_container_name=${DOCKER_BASE_CONTAINER_NAME}Green
new_port='8081'
if sudo docker ps --format '{{.Names}}' | grep -q "${DOCKER_BASE_CONTAINER_NAME}Green"; then
  old_container_name=${DOCKER_BASE_CONTAINER_NAME}Green
  old_port='8081'
  new_container_name=${DOCKER_BASE_CONTAINER_NAME}Blue
  new_port='8080'
fi
new_tag=$(echo "$image_info" | sort -k2 -r | awk '{print $1}' | head -n 1)
old_tag=$(sudo docker inspect --format='{{.Config.Image}}' $old_container_name | awk -F: '{print $2}')

echo "Deploy the new Docker image ($new_tag:$new_port)"
sudo docker pull $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/$DOCKER_REGISTRY_NAME:$new_tag
sudo docker network create $DOCKER_NETWORK || true
sudo docker network connect $DOCKER_NETWORK $APP_NAME-db || true
sudo docker run -d -p $new_port:8080 --restart always --name $new_container_name --network=$DOCKER_NETWORK $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$DOCKER_REGISTRY_NAME:$new_tag
checkDeploymentSuccess "http://127.0.0.1:$new_port"

echo "Switch from old container ($old_tag:$old_port) to new container ($new_tag:$new_port)"
sed -i "s/127.0.0.1:$old_port;/127.0.0.1:$new_port;/" "/etc/nginx/sites-available/reverseproxy"
sudo systemctl restart nginx
checkDeploymentSuccess "https://backend.$APP_DOMAIN_NAME"

echo "Cleaning local and registry Docker images"
sudo docker stop $old_container_name || true
sudo docker rm $old_container_name || true
sudo docker image prune -a -f
sudo docker system prune -a -f
image_tags=$(aws ecr list-images --region $AWS_REGION --repository-name $DOCKER_REGISTRY_NAME --query 'imageIds[].imageTag' --output text)
for image_tag in $image_tags; do
  if [ "$image_tag" != "$new_tag" ] && [ "$image_tag" != "$old_tag" ]; then
    aws ecr batch-delete-image --region $AWS_REGION --repository-name $DOCKER_REGISTRY_NAME --image-ids imageTag=$image_tag
  fi
done
