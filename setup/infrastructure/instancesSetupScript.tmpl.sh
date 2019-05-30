#!/bin/bash

#==========================================================================================================
# Note: to apply changes performed in this script: force running EC2 instances to terminate via AWS Console
#==========================================================================================================

#Install CodeDeploy agent
sudo apt-get update
sudo apt-get install -y ruby wget default-jre
cd /home/ubuntu/
wget https://aws-codedeploy-eu-central-1.s3.eu-central-1.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
rm ./install
sed -i 's/CODEDEPLOY_USER=""/CODEDEPLOY_USER="ubuntu"/g' /etc/init.d/codedeploy-agent
sudo chown ubuntu:ubuntu -R /opt/codedeploy-agent/
sudo chown ubuntu:ubuntu -R /var/log/aws/
sudo service codedeploy-agent start

#Install Java
sudo apt-get install -y default-jre

#Setup EFS
sudo apt-get install -y nfs-common
sudo mkdir ./efs
sudo mount -t nfs4 -o nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2,noresvport ${efsDnsName}:/ ./efs
sudo chown ubuntu:ubuntu -R ./efs
