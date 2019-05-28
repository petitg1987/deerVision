#!/bin/bash

set -e
cd "$(dirname "$0")"
export AWS_DEFAULT_PROFILE=releasemgt

appName=$1
packageName="uninitialized"

function checkAppName() {
    regexAppName="^[0-9a-z\-]+$";
    if [[ ! "$appName" =~ $regexAppName ]]; then
        echo "Application name not matching regex $regexAppName"
        exit 1
    fi
}

function buildProject() {
    mvn clean package -f ../../pom.xml
}

function copyPackageInS3() {
    packagePattern=(../../target/urchin-release-mgt-*.zip)
    packagePath="${packagePattern[0]}"
    packageName="${packagePath##*/}"
    echo "Copying '$packagePath' in S3 bucket '${appName}-releasemgt'"
    aws s3 cp ${packagePath} s3://${appName}-releasemgt/releases/${packageName}
}

function triggerCodeDeploy() {
    aws deploy create-deployment \
      --region eu-west-3 \
      --application-name ${appName}RelMgtApp \
      --deployment-config-name CodeDeployDefault.AllAtOnce \
      --deployment-group-name ${appName}RelMgtDeploymentGroup \
      --s3-location bucket=${appName}-releasemgt,bundleType=zip,key=releases/${packageName}
}

checkAppName

buildProject
copyPackageInS3
triggerCodeDeploy
