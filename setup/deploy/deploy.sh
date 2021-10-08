#!/bin/bash

set -e
cd "$(dirname "$0")"
export AWS_DEFAULT_PROFILE=deervision

appName="deervision"
packageName="deer-vision-1.0.0.zip"

function buildPackage() {
    mvn clean package -f ../../pom.xml

    cd ../../target/
    echo "binary.aws-bucket-name: \"${appName}\"" > application-appinfo.yml
    zip -r ${packageName} application-appinfo.yml
    rm application-appinfo.yml
    cd ../setup/deploy/
}

function copyPackageInS3() {
    packagePath="../../target/${packageName}"
    echo "Copying '$packagePath' in S3 bucket '${appName}'"
    aws s3 cp ${packagePath} s3://${appName}/releases/${packageName}
}

function triggerCodeDeploy() {
    aws deploy create-deployment \
      --region eu-central-1 \
      --application-name ${appName}App \
      --deployment-config-name CodeDeployDefault.AllAtOnce \
      --deployment-group-name ${appName}DeploymentGroup \
      --s3-location bucket=${appName},bundleType=zip,key=releases/${packageName}
}

buildPackage
copyPackageInS3
triggerCodeDeploy
