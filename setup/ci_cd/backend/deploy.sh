#!/bin/bash

set -e
cd "$(dirname "$0")"
repository_dir=$1

function checkDeploymentSuccess() {
  base_url=$1
  max_attempts=50
  success=false
  for ((i=1; i<=max_attempts; i++)); do
    http_status=$(curl -s -o /dev/null -w "%{http_code}" "$base_url"/api/test/deploy || true)
    echo "Attempt $i - Request on $base_url return HTTP code $http_status"
    if [ "$http_status" -eq 200 ]; then
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

echo " "
echo "-----------------------------------------------------"
echo "1. Starting to build the backend image"
echo "-----------------------------------------------------"
cd "$repository_dir"/deer-vision-backend/
mvn -B clean package
rm -rf /tmp/img_backend/
mkdir -p /tmp/img_backend/
cp ./target/deer-vision*.jar /tmp/img_backend/deer-vision-backend.jar
cp ../setup/ci_cd/backend/Dockerfile /tmp/img_backend/Dockerfile
docker build -t deer-vision-backend:latest /tmp/img_backend

echo " "
echo "-----------------------------------------------------"
echo "2. Get backend deployment information"
echo "-----------------------------------------------------"
old_container_name=deer-vision-backend-blue
old_port='13002'
new_container_name=deer-vision-backend-green
new_port='13003'
greenContainerExist=$(docker ps --format '{{.Names}}' | grep "deer-vision-backend-green" || true)
if [[ -n "$greenContainerExist" ]]; then
  old_container_name=deer-vision-backend-green
  old_port='13003'
  new_container_name=deer-vision-backend-blue
  new_port='13002'
fi
echo "  - Old container name: ${old_container_name}:${old_port}, New container name: ${new_container_name}:${new_port}"

echo " "
echo "-----------------------------------------------------"
echo "3. Loading backend secret parameters"
echo "-----------------------------------------------------"
dbPassword=$(cat /data/ci_cd/secret/deerVisionDbPassword)
adminPassword=$(cat /data/ci_cd/secret/deerVisionAdminPassword)
adminJwtToken=$(cat /data/ci_cd/secret/deerVisionAdminJwtSecret)

echo " "
echo "-----------------------------------------------------"
echo "4. Deploy the new Docker image (${new_container_name}:${new_port})"
echo "-----------------------------------------------------"
docker network create "deer-vision-network" || true
docker network connect "deer-vision-network" "deer-vision-db" || true
docker stop "$new_container_name" || true #useless except when script crashed
docker rm "$new_container_name" || true #useless except when script crashed
docker run -d \
    -p ${new_port}:8080 \
    -e DB_PASSWORD="$dbPassword" \
    -e ADMIN_PASSWORD="$adminPassword" \
    -e ADMIN_JWT_SECRET="$adminJwtToken" \
    --restart always \
    --name "${new_container_name}" \
    --network="deer-vision-network" \
    deer-vision-backend:latest
checkDeploymentSuccess "http://127.0.0.1:$new_port"

echo " "
echo "-----------------------------------------------------"
echo "5. Switch from old container ($old_container_name:$old_port) to new container ($new_container_name:$new_port)"
echo "-----------------------------------------------------"
#TODO sudo sed -i "s/127.0.0.1:$old_port;/127.0.0.1:$new_port;/" "/etc/nginx/sites-available/reverseproxy"
#TODO sudo systemctl restart nginx
docker stop $old_container_name || true
docker rm $old_container_name || true
#TODO checkDeploymentSuccess "https://backend.$APP_DOMAIN_NAME"

echo " "
echo "-----------------------------------------------------"
echo "6. Cleaning local Docker images"
echo "-----------------------------------------------------"
docker image prune -a -f
docker system prune -a -f
