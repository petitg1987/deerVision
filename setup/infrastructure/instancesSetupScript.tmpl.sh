#!/bin/bash -x

#==========================================================================================================
# Note: to apply changes performed in this script: force running EC2 instances to terminate via AWS Console
#==========================================================================================================

#Install required software
sudo apt-get update
sudo apt-get install -y ruby wget default-jre nfs-common

#Install CodeDeploy agent
cd /home/ubuntu/
wget https://aws-codedeploy-eu-central-1.s3.eu-central-1.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
rm ./install
sudo service codedeploy-agent start

#Setup EFS
sudo mkdir ./efs
sudo mount -t nfs4 -o nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2,noresvport ${efsDnsName}:/ ./efs
sudo chown ubuntu:ubuntu -R ./efs
