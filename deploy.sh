#!/bin/bash

#Pre-requisites:
# Setup EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-install.html
# Configure EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-configuration.html

if [[ -z $1 ]]; then
    echo "Application name is required (example: GreenCityReleaseMgt)"
    exit 1
fi

if ! [[ "$2" =~ ^(delete|init|deploy)$ ]]; then
    echo "Action must be defined: init, deploy, delete"
    exit 1
fi

appVersion=`cat pom.xml | grep -Eo "[0-9]\.[0-9]\.[0-9]</version>" | head -1 | cut -d'<' -f1`
echo "Start '$2' action on application version $appVersion"

cd "$(dirname "$0")"
mvn clean install

if [[ "$2" == "init" ]]; then
    eb init $1
    eb create prod-env
elif [[ "$2" == "deploy" ]]; then
    eb deploy prod-env
elif [[ "$2" == "delete" ]]; then
    eb terminate prod-env
    echo "Please, delete manually AWS S3 bucket linked to elastic beanstalk..."
fi
