# Setup infrastructure
## Pre-requisites on AWS
* Create IAM User named "release-mgt-user" with programmatic access and having "AdministratorAccess" IAM policy
* Add IAM user keys in ~/.aws/credentials under "[releasemgt]" profile
* Create EC2 key pair named "releasemgt" and add them in ~/.ssh/
* Delete the default VPC (optional)
* Create DNS "releasemgt.net" by using Route 53
* Request a public certificate for following domains on Amazon Certificate Manager: releasemgt.net & *.releasemgt.net

## Create new infrastructure
* Create infrastructure: `./infrastructure/infra.sh create [APP_NAME] [CIDR_PREFIX]`
    * Example: `./infrastructure/infra.sh init greencity 10.0`

## Update infrastructure
* Update infrastructure: `./infrastructure/infra.sh update [APP_NAME]`

## Destroy infrastructure:
* Destroy infrastructure: `./infrastructure/infra.sh destroy [APP_NAME]`
* Destroy infrastructure and configurations: `./infrastructure/infra.sh destroyAll [APP_NAME]`

# Deploy/update application
* TODO