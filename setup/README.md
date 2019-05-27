# Setup infrastructure
## Pre-requisites
* AWS
  * Create IAM User named "release-mgt-user" with programmatic access and having "AdministratorAccess" IAM policy
  * Add the IAM user keys in:
    * `~/.aws/credentials` for "releasemgt" profile
    * `src/main/resources/application-awssecurity.yml` for "binary.aws-access-key-id" and "binary.aws-secret-access-key" properties
  * Create EC2 key pair named "releasemgt" and add files in `~/.ssh/`
  * Create DNS "releasemgt.net" by using Route 53
  * Request a public certificate for following domains on Amazon Certificate Manager: releasemgt.net & *.releasemgt.net
  * Delete the default VPC (optional)
* Terraform
  * Terraform >= v0.12 is installed

## Manage infrastructure
### Create
* Create infrastructure: `./infrastructure/infra.sh create [APP_NAME] [CIDR_PREFIX]`
    * Example: `./infrastructure/infra.sh create greencity 10.0`

### Update
* Update infrastructure: `./infrastructure/infra.sh update [APP_NAME]`

### Destroy
* Destroy infrastructure: `./infrastructure/infra.sh destroy [APP_NAME]`
* Destroy infrastructure and configurations: `./infrastructure/infra.sh destroyAll [APP_NAME]`

# Deploy the application
* TODO