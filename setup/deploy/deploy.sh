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

function buildPackage() {
    mvn clean package -f ../../pom.xml

    cd ../../target/
    genericPackagePattern=(urchin-release-mgt-*.zip)
    genericPackageName="${genericPackagePattern[0]}"
    packageName=${appName}-${genericPackageName}

    cp ${genericPackageName} ${packageName}
    echo "binary.aws-bucket-name: \"${appName}-releasemgt\"" > application-appinfo.yml
    zip -r ${packageName} application-appinfo.yml
    rm application-appinfo.yml
    cd ../setup/deploy/
}

function copyPackageInS3() {
    packagePath="../../target/${packageName}"
    echo "Copying '$packagePath' in S3 bucket '${appName}-releasemgt'"
    aws s3 cp ${packagePath} s3://${appName}-releasemgt/releases/${packageName}
}

function triggerCodeDeploy() {
    aws deploy create-deployment \
      --region eu-central-1 \
      --application-name ${appName}RelMgtApp \
      --deployment-config-name CodeDeployDefault.AllAtOnce \
      --deployment-group-name ${appName}RelMgtDeploymentGroup \
      --s3-location bucket=${appName}-releasemgt,bundleType=zip,key=releases/${packageName}
}

checkAppName

buildPackage
copyPackageInS3
triggerCodeDeploy
