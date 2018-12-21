#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
# Pre-requisites:
#   - Define AWS credential and region: https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html
#   - Setup EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-install.html
#   - Configure EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-configuration.html
#   - DNS:
#       - Create DNS "releasemgt.net" by using Route 53
#       - Configure DNS by using Route 53 for hosted zones:
#           - Add "A" record for [APP_NAME].releasemgt.net with Alias value: [APP_NAME]releasemgt.eu-west-3.elasticbeanstalk.com
#           - Add "CNAME" record for www.[APP_NAME].releasemgt.net with value: [APP_NAME]releasemgt.eu-west-3.elasticbeanstalk.com
#   - HTTPS Certificate:
#       - Request a public certificate for following domains: releasemgt.net & *.releasemgt.net
#       - Update "CERTIFICATE_ARN" variable with certificate ARN value
#
# Configuration
CERTIFICATE_ARN="arn:aws:acm:eu-west-3:496124100072:certificate/5d207839-3699-4bc7-b207-6f52ddd42fd3"
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

set -e
cd "$(dirname "$0")"

read -p "Application name to manage (example: GreenCity): " appName
read -p "Action to execute (init/deploy/delete): " actionName

fullAppName="${appName}ReleaseMgt"
appVersion=`cat pom.xml | grep -Eo "[0-9]\.[0-9]\.[0-9]</version>" | head -1 | cut -d'<' -f1`
cnamePrefix=`echo "$fullAppName" | tr '[:upper:]' '[:lower:]'`
echo "Execute '$actionName' action on application '$fullAppName/$appVersion' with CNAME prefix '$cnamePrefix'"

echo " - Replace ARN certificate in ./.ebextensions/options.config"
sed -i "s@.*SSLCertificateId.*@    SSLCertificateId: ${CERTIFICATE_ARN}@" ./.ebextensions/options.config

echo " - Build application"
mvn clean install > /tmp/build.txt

echo " - Execute action $actionName on AWS"
if [[ "$actionName" == "init" ]]; then
    rm -rf ./.elasticbeanstalk/config.yml
    mkdir -p ./.elasticbeanstalk
    echo "deploy:" >> ./.elasticbeanstalk/config.yml
    echo "  artifact: target/urchin-release-mgt.zip" >> ./.elasticbeanstalk/config.yml

    eb init ${fullAppName}
    eb create prod-env --cname ${cnamePrefix}
elif [[ "$actionName" == "deploy" ]]; then
    eb deploy prod-env
elif [[ "$actionName" == "delete" ]]; then
    eb terminate prod-env --all
else
    echo "Unknown action $actionName"
    exit 1
fi

echo "DEPLOYMENT SUCCESS !"
