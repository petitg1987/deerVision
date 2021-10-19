#!/usr/bin/env bash

while [[ true ]]
do
    httpCodeStatus=`curl -I -X POST -o /dev/null -w "%{http_code}" -s http://localhost:8080/api/admin/login?password=abc`
    echo "HTTP code status: $httpCodeStatus"

    if [[ "$httpCodeStatus" = "200" ]]
    then
        echo "Server is running"
        exit 0
    else
        echo "Check server is running?"
        sleep 1s
    fi
done
