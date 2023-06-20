#!/bin/bash

set -e
cd "$(dirname "$0")"
export AWS_DEFAULT_PROFILE=deervision

appName="deervision"
backendBucketName="${appName}-backend"
backendPackageName="deer-vision-1.0.0.zip"
frontendBucketName="${appName}-frontend"

function buildBackendPackage() {
    mvn clean package -f ../../deer-vision-backend/pom.xml

    cd ../../deer-vision-backend/target/
    echo "binary.aws-bucket-name: \"${backendBucketName}\"" > application-appinfo.yml
    zip -r ${backendPackageName} application-appinfo.yml
    rm application-appinfo.yml
    cd ../../setup/deploy/
}

function copyBackendPackageInS3() {
    packagePath="../../deer-vision-backend/target/${backendPackageName}"
    echo "Copying '$packagePath' in S3 bucket '${backendBucketName}'"
    aws s3 cp ${packagePath} s3://${backendBucketName}/releases/${backendPackageName}
}

function triggerCodeDeploy() {
    aws deploy create-deployment \
      --region eu-central-1 \
      --application-name ${appName}App \
      --deployment-config-name CodeDeployDefault.AllAtOnce \
      --deployment-group-name ${appName}DeploymentGroup \
      --s3-location bucket=${backendBucketName},bundleType=zip,key=releases/${backendPackageName}
}

function buildFrontPackage() {
    cd ../../deer-vision-frontend/
    yarn build
    cd ../setup/deploy/
}

function copyFrontendFilesInS3() {
    originalFilesPath="../../deer-vision-frontend/build/"
    filesPath="../../deer-vision-frontend/builds3/"

    #Sync via checksum to not copy files which are not modified (only modification date time is different)
    #Indeed, "aws s3 sync" consider a file as modified when the modification date time is updated
    mkdir -p ${filesPath}
    rsync -v --checksum --delete --recursive ${originalFilesPath} ${filesPath}

    echo "Sync files '$filesPath' from S3 bucket '${frontendBucketName}'"
    aws s3 sync --delete --exclude "_source/*" --exclude "*.html" --cache-control max-age=31536000 "${filesPath}" "s3://${frontendBucketName}/"
    aws s3 sync --delete --exclude "*" --include "*.html" --cache-control no-cache --content-type "text/html" "${filesPath}" "s3://${frontendBucketName}/" #No cache for html files entry point
}

function invalidCloudFrontCache() {
    cloudDistributionId=$(aws cloudfront list-distributions | jq -r '.DistributionList.Items[] | select (.Aliases.Items[0]=="deervision.studio") | .Id')
    echo "Invaliding cache for distribution id: ${cloudDistributionId}"
    aws cloudfront create-invalidation --distribution-id ${cloudDistributionId} --paths "/*"
}

if [[ "$1" == "backend" ]]; then
  buildBackendPackage
  copyBackendPackageInS3
  triggerCodeDeploy
elif [[ "$1" == "frontend" ]]; then
  buildFrontPackage
  copyFrontendFilesInS3
  invalidCloudFrontCache
else
  echo "First parameter must be either 'backend' or 'frontend'"
fi
