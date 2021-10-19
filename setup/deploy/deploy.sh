#!/bin/bash

set -e
cd "$(dirname "$0")"
export AWS_DEFAULT_PROFILE=deervision

appName="deervision"
packageName="deer-vision-1.0.0.zip"

function buildPackage() {
    mvn clean package -f ../../deer-vision-backend/pom.xml

    cd ../../deer-vision-backend/target/
    echo "binary.aws-bucket-name: \"${appName}\"" > application-appinfo.yml
    zip -r ${packageName} application-appinfo.yml
    rm application-appinfo.yml
    cd ../../setup/deploy/
}

function copyPackageInS3() {
    packagePath="../../deer-vision-backend/target/${packageName}"
    bucketName="${appName}-backend"
    echo "Copying '$packagePath' in S3 bucket '${bucketName}'"
    aws s3 cp ${packagePath} s3://${bucketName}/releases/${packageName}
}

function triggerCodeDeploy() {
  bucketName="${appName}-backend"
    aws deploy create-deployment \
      --region eu-central-1 \
      --application-name ${appName}App \
      --deployment-config-name CodeDeployDefault.AllAtOnce \
      --deployment-group-name ${appName}DeploymentGroup \
      --s3-location bucket=${bucketName},bundleType=zip,key=releases/${packageName}
}

buildPackage
copyPackageInS3
triggerCodeDeploy
