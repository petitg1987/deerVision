#!/bin/bash

set -e
cd "$(dirname "$0")"



# 1) Publish on S3
# 2) Execute:
#
# aws deploy create-deployment \
#  --application-name greencityRelMgtApp \
#  --deployment-config-name CodeDeployDefault.AllAtOnce \
#  --deployment-group-name greencityRelMgtDeploymentGroup \
#  --s3-location bucket=greencity-releasemgt,bundleType=zip,key=releases/urchin-release-mgt-1.0.0.zip

