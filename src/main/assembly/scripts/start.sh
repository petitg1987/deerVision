#!/usr/bin/env bash

cd /home/ubuntu/app/
sudo /usr/bin/java -jar -Dserver.port=80 urchin-release-mgt.jar > /home/ubuntu/application.log 2>&1 &
