#!/bin/bash

#Note: to apply changes performed in this script: force running EC2 instances to terminate via AWS Console

#Install CodeDeploy agent
sudo apt-get update
sudo apt-get install -y ruby wget
cd /home/ubuntu/
wget https://aws-codedeploy-eu-west-3.s3.eu-west-3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo service codedeploy-agent start

#TODO remove
sudo apt-get install -y apache2
