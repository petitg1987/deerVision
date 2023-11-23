# Pre-requisites
* AWS
  * Create "IAM Group" named "deer-vision-group" having "AdministratorAccess" IAM policy
  * Create "IAM User" named "deer-vision-user" with programmatic access and in "deer-vision-group" group
  * Add the IAM user keys in `~/.aws/credentials` for "deervision" profile
  * Add the region "eu-central-1" in `~/.aws/config` for "deervision" profile
  * Create EC2 key pair named "deervision" and add file "deervision.pem" with permission '600' in `~/.ssh/`
  * Register domain "deervision.studio" by using Route 53 and enable "Transfer lock"
  * In "N.Virgina" region: create and validate an ACM certificate for "deervision.studio" and with additional domain name "www.deervision.studio"
  * Delete the default VPC (optional)
  * Add parameters in AWS System Manager Parameter Store
    * Type=Standard, Name=deervisionDbPassword, Value=clear DB password
    * Type=Standard, Name=deervisionAdminPassword, Value=BCrypt hashed password with 10 rounds
    * Type=Standard, Name=deervisionAdminJwtSecret, Value=JWT secret
  * Add secret in GitHub
    * Name=EC2_SSH_PRIVATE_KEY, Value=EC2 private key (`~/.ssh/deervision.pem`)
* Install AWS CLI: `snap install aws-cli`
* Install Maven: `sudo apt install maven`

# Manage infrastructure
* Create infrastructure: `./infrastructure/infra.sh create`
* Update infrastructure: `./infrastructure/infra.sh update`
* Destroy infrastructure: `./infrastructure/infra.sh destroy`
* Destroy infrastructure and configurations: `./infrastructure/infra.sh destroyAll`
* Others:
  * See plan: `./infrastructure/infra.sh plan`
  * Force instance re-creation: `./infrastructure/infra.sh recreateInstance`

# Deploy the application
* Commit on master branch to trigger the GitHub actions
* Check server:
  ```bash
  EC2_PUBLIC_IP=$(aws ec2 describe-instances --filters Name=key-name,Values=deervision Name=instance-state-name,Values=running --query "Reservations[0].Instances[0].PublicIpAddress" --output text)
  ssh -o "StrictHostKeyChecking=no" -i /home/greg/.ssh/deervision.pem "ubuntu@${EC2_PUBLIC_IP}"
  ```
