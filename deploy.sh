#!/bin/bash

#Pre-requisites:
# Define AWS credential and region: https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html
# Setup EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-install.html
# Configure EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-configuration.html

if [[ -z $1 ]]; then
    echo "Application name is required (example: GreenCity)"
    exit 1
fi

if ! [[ "$2" =~ ^(delete|init|deploy)$ ]]; then
    echo "Action must be defined: init, deploy, delete"
    exit 1
fi

appName="$1ReleaseMgt"
appVersion=`cat pom.xml | grep -Eo "[0-9]\.[0-9]\.[0-9]</version>" | head -1 | cut -d'<' -f1`
cnamePrefix=`echo "$appName" | tr '[:upper:]' '[:lower:]'`
echo "Execute '$2' action on application '$appName/$appVersion' with CNAME prefix '$cnamePrefix'"

cd "$(dirname "$0")"
mvn clean install

if [[ "$2" == "init" ]]; then
    rm -rf ./.elasticbeanstalk/config.yml
    mkdir -p ./.elasticbeanstalk
    echo "deploy:" >> ./.elasticbeanstalk/config.yml
    echo "  artifact: target/urchin-release-mgt.zip" >> ./.elasticbeanstalk/config.yml

    eb init ${appName}
    eb create prod-env --cname ${cnamePrefix}
elif [[ "$2" == "deploy" ]]; then
    eb deploy prod-env
elif [[ "$2" == "delete" ]]; then
    eb terminate prod-env --all
    echo "AWS S3 bucket linked to elastic beanstalk can be deleted"
fi
