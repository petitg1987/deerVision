#!/usr/bin/env bash

cd /home/ubuntu/app/
sudo /usr/bin/java -jar -Dserver.port=8080 deer-vision.jar > /home/ubuntu/application.log 2>&1 &
