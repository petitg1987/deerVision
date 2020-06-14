# Pre-requisites
* AWS
  * Create "IAM Group" named "release-mgt-group" having "AdministratorAccess" IAM policy
  * Create "IAM User" named "release-mgt-user" with programmatic access and in "release-mgt-group" group
  * Add the IAM user keys in:
    * `~/.aws/credentials` for "releasemgt" profile
    * `src/main/resources/application-awssecurity.yml` for "binary.aws-access-key-id" and "binary.aws-secret-access-key" properties
  * Create EC2 key pair named "releasemgt" and add files in `~/.ssh/`
  * Create DNS "releasemgt.net" by using Route 53
  * Delete the default VPC (optional)
* Terraform: terraform >= v0.12 must be installed
* Install AWS CLI: `snap install aws-cli`
* Install Maven: `sudo apt install maven`

# Manage infrastructure
* Create infrastructure: `./infrastructure/infra.sh create [APP_NAME] [CIDR_PREFIX]`
    * Example: `./infrastructure/infra.sh create greencity 10.0`
* Update infrastructure: `./infrastructure/infra.sh update [APP_NAME]`
* Destroy infrastructure: `./infrastructure/infra.sh destroy [APP_NAME]`
* Destroy infrastructure and configurations: `./infrastructure/infra.sh destroyAll [APP_NAME]`

# Deploy the application
* Execute: `./deploy/deploy.sh [APP_NAME]`
