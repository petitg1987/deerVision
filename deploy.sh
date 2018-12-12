#!/bin/bash

#Pre-requisites:
# Setup EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-install.html
# Configure EB CLI: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-configuration.html
# cd [PROJECT-DIR] && eb init [APP-NAME] (example: cd /home/greg/Project/urchin/urchinReleaseMgt && eb init GreenCityReleaseMgt)

cd "$(dirname "$0")"

mvn clean install
eb create prod-env #TODO use: "eb create prod-env  --elb-type classic" ???
#TODO: update config.yml with correct version + Dockerfile ?
eb deploy prod-env --version GreenCityReleaseMgt