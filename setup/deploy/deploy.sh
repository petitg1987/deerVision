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
    filesPath="../../deer-vision-frontend/build/"
    echo "Copying files '$filesPath' in S3 bucket '${backendBucketName}'"
    aws s3 cp ${filesPath} s3://${frontendBucketName}/ --recursive
}

buildBackendPackage
copyBackendPackageInS3
triggerCodeDeploy

buildFrontPackage
copyFrontendFilesInS3
