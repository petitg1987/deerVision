# Pre-requisites
* AWS
  * Create "IAM Group" named "deer-vision-group" having "AdministratorAccess" IAM policy
  * Create "IAM User" named "deer-vision-user" with programmatic access and in "deer-vision-group" group
  * Add the IAM user keys in `~/.aws/credentials` for "deervision" profile
  * Create EC2 key pair named "deervision" and add file "deervision.pem" with permission '600' in `~/.ssh/`
  * Register domain "deervision.studio" by using Route 53 and enable "Transfer lock"
  * Delete the default VPC (optional)
* Terraform: terraform >= v0.12 must be installed
* Install AWS CLI: `snap install aws-cli`
* Install Maven: `sudo apt install maven`

# Manage infrastructure
* Create infrastructure: `./infrastructure/infra.sh create`
* Update infrastructure: `./infrastructure/infra.sh update`
* Destroy infrastructure: `./infrastructure/infra.sh destroy`
* Destroy infrastructure and configurations: `./infrastructure/infra.sh destroyAll`

# Deploy the application
* Execute: `./deploy/deploy.sh`
* Check log on server: `ssh -o "StrictHostKeyChecking=no" -i /home/greg/.ssh/deervision.pem "ubuntu@[PUBLIC_IP]"`
