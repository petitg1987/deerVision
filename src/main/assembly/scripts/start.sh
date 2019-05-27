#!/usr/bin/env bash

cd /home/ubuntu/
sudo /usr/bin/java -jar -Dserver.port=80 urchin-release-mgt.jar >application.log 2>&1 &
