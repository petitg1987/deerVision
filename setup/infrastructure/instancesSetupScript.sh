#!/bin/bash

#Note: to apply changes performed in this script: force running EC2 instances to terminate via AWS Console

#Install CodeDeploy agent
sudo apt-get update
sudo apt-get install -y ruby wget default-jre
cd /home/ubuntu/
wget https://aws-codedeploy-eu-central-1.s3.eu-central-1.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo service codedeploy-agent start

#Install Java
sudo apt-get install -y default-jre
