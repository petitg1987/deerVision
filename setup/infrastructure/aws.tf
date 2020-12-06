##########################################################################################
# VARIABLES
##########################################################################################
variable "appName" {
  description = "Application name"
  type = string
}

variable "cidrPrefix" {
  description = "CIDR prefix (e.g.: 10.0)"
  type = string
}

variable "region" {
  description = "Run the EC2 instances in this region"
  type = string
  default = "eu-central-1"
}

variable "availabilityZones" {
  description = "Run the EC2 instances in these availability zones"
  type = list(string)
  default = ["eu-central-1a", "eu-central-1b", "eu-central-1c"]
}

##########################################################################################
# CREDENTIAL & LOCATION
##########################################################################################
provider "aws" {
  region = var.region
  shared_credentials_file = "~/.aws/credentials"
  profile = "releasemgt"
  version = "~> 2.23"
}

data "aws_iam_user" "rlmgt_user" {
  user_name = "release-mgt-user"
}

##########################################################################################
# NETWORK & ACCESS
##########################################################################################
resource "aws_vpc" "rlmgt_vpc" {
  cidr_block = "${var.cidrPrefix}.0.0/16"
  enable_dns_hostnames = true
  tags = {
    Name = "${var.appName}RelMgtVPC"
    Application = var.appName
  }
}

resource "aws_internet_gateway" "rlmgt_igw" {
  vpc_id = aws_vpc.rlmgt_vpc.id
  tags = {
    Name = "${var.appName}RelMgtIGW"
    Application = var.appName
  }
}

resource "aws_route_table" "rlmgt_route" {
  vpc_id = aws_vpc.rlmgt_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.rlmgt_igw.id
  }
  tags = {
    Name = "${var.appName}RelMgtRoute"
    Application = var.appName
  }
}

resource "aws_subnet" "rlmgt_public_subnet" {
  count = length(var.availabilityZones)
  vpc_id = aws_vpc.rlmgt_vpc.id
  cidr_block = "${var.cidrPrefix}.${count.index}.0/24"
  map_public_ip_on_launch = true
  availability_zone = var.availabilityZones[count.index]
  tags = {
    Application = var.appName
  }
}

resource "aws_route_table_association" "rlmgt_route_association" {
  count = length(var.availabilityZones)
  subnet_id = aws_subnet.rlmgt_public_subnet[count.index].id
  route_table_id = aws_route_table.rlmgt_route.id
}

resource "aws_network_acl" "rlmgt_network_acl" {
  vpc_id = aws_vpc.rlmgt_vpc.id
  subnet_ids = aws_subnet.rlmgt_public_subnet.*.id
  ingress { #port for HTTPS (website request)
    rule_no = 100
    from_port = 443
    to_port = 443
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  ingress { #port for HTTP (website request)
    rule_no = 110
    from_port = 80
    to_port = 80
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  ingress { #port for SSH (request)
    rule_no = 120
    from_port = 22
    to_port = 22
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  ingress { #ephemeral ports for HTTP (EC2 internet response) and HTTPS (S3 response)
    rule_no = 130
    from_port = 1024
    to_port = 65535
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  egress { #ephemeral ports for SSH (response) and HTTP/HTTPS (website response)
    rule_no = 100
    from_port = 1024
    to_port = 65535
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  egress { #port for HTTPS (S3 request)
    rule_no = 110
    from_port = 443
    to_port = 443
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  egress { #port for HTTP (EC2 internet request)
    rule_no = 120
    from_port = 80
    to_port = 80
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  tags = {
    Name = "${var.appName}RelMgtNetworkACL"
    Application = var.appName
  }
}

##########################################################################################
# EFS
##########################################################################################
resource "aws_efs_file_system" "rlmgt_efs" {
  creation_token = "${var.appName}RelMgEfsToken"
  tags = {
    Name = "${var.appName}RelMgtEfsName"
    Application = var.appName
  }
}

resource "aws_security_group" "rlmgt_efs_sg" {
  name = "${var.appName}RelMgtEfsSG"
  description = "Release Mgt EFS Security Group"
  vpc_id = aws_vpc.rlmgt_vpc.id
  ingress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    security_groups = [aws_security_group.rlmgt_instance_sg.id]
    description = "Instances can access to EFS"
  }
  tags = {
    Name = "${var.appName}RelMgtInstanceSG"
    Application = var.appName
  }
}

resource "aws_efs_mount_target" "rlmgt_efs_mount_target" {
  count = length(var.availabilityZones)
  file_system_id = aws_efs_file_system.rlmgt_efs.id
  subnet_id = aws_subnet.rlmgt_public_subnet[count.index].id
  security_groups = [aws_security_group.rlmgt_efs_sg.id]
}

##########################################################################################
# INSTANCES
##########################################################################################
data "aws_ami" "ubuntu" {
  most_recent = true
  filter {
    name = "name"
    #New Ubuntu 20.04: "ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*". Doesn't work with code deploy script yet.
    values = ["ubuntu/images/hvm-ssd/ubuntu-bionic-18.04-amd64-server-*"]
  }
  filter {
    name = "virtualization-type"
    values = ["hvm"]
  }
  owners = ["099720109477"]
}

resource "aws_iam_role" "rlmgt_instance_role" {
  name = "${var.appName}RelMgtInstanceRole"
  description = "Allow EC2 instances to access to S3/SNS/SQS (code deploy agent) and Route53 (let's encrypt)"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
  tags = {
    Application = var.appName
  }
}

resource "aws_security_group" "rlmgt_instance_sg" {
  name = "${var.appName}RelMgtInstanceSG"
  description = "Release Mgt Instance Security Group"
  vpc_id = aws_vpc.rlmgt_vpc.id
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "SSH"
  }
  ingress {
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTPS requests from Internet"
  }
  ingress {
    from_port = 2049
    to_port = 2049
    protocol = "tcp"
    cidr_blocks = [aws_vpc.rlmgt_vpc.cidr_block] #Alternative: use EFS security group
    description = "EFS"
  }
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Connection from EC2 to Internet"
  }
  tags = {
    Name = "${var.appName}RelMgtInstanceSG"
    Application = var.appName
  }
}

resource "aws_iam_role_policy_attachment" "AmazonEC2RoleforAWSCodeDeploy" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforAWSCodeDeploy"
  role = aws_iam_role.rlmgt_instance_role.name
}

resource "aws_iam_role_policy_attachment" "AutoScalingNotificationAccessRole" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AutoScalingNotificationAccessRole"
  role = aws_iam_role.rlmgt_instance_role.name
}

resource "aws_iam_role_policy_attachment" "CloudWatchAgentAdminPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentAdminPolicy"
  role = aws_iam_role.rlmgt_instance_role.name
}

resource "aws_iam_role_policy_attachment" "AmazonRoute53FullAccess" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonRoute53FullAccess"
  role = aws_iam_role.rlmgt_instance_role.name
}

resource "aws_iam_instance_profile" "rlmgt_instance_profile" {
  name = "${var.appName}RelMgtInstanceProfile"
  role = aws_iam_role.rlmgt_instance_role.name
}

data "aws_iam_role" "AWSServiceRoleForAutoScaling" {
  name = "AWSServiceRoleForAutoScaling"
}

resource "aws_kms_key" "rlmgt_ebs_kms_key" {
  description = "${var.appName} EBS KMS Key"
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Id": "key-default-1",
    "Statement": [
        {
            "Sid": "Enable IAM User Permissions",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                  "${data.aws_iam_role.AWSServiceRoleForAutoScaling.arn}",
                  "${data.aws_iam_user.rlmgt_user.arn}"
                ]
            },
            "Action": "kms:*",
            "Resource": "*"
        }
    ]
}
EOF
  tags = {
    Application = var.appName
  }
}

resource "aws_kms_alias" "rlmgt_ebs_kms_key_alias" {
  name = "alias/${var.appName}EbsKmsKey"
  target_key_id = aws_kms_key.rlmgt_ebs_kms_key.key_id
}

resource "aws_instance" "rlmgt_instance" {
  ami = data.aws_ami.ubuntu.id
  instance_type = "t2.micro"
  key_name = "releasemgt"
  iam_instance_profile = aws_iam_instance_profile.rlmgt_instance_profile.name
  subnet_id = aws_subnet.rlmgt_public_subnet[0].id
  vpc_security_group_ids = [aws_security_group.rlmgt_instance_sg.id]
  user_data = base64encode(templatefile("${path.module}/instancesSetupScript.tmpl.sh", {
    requestsBySecond = 5,
    requestsBurst = 10,
    efsDnsName = aws_efs_file_system.rlmgt_efs.dns_name,
    logGroupName = "${var.appName}RelMgtLogsGroup",
    logStreamNamePrefix = "${var.appName}RelMgtLogsStream"
  }))
  ebs_block_device {
    device_name = "/dev/sda1"
    delete_on_termination = "true"
    encrypted = "true"
    kms_key_id = aws_kms_key.rlmgt_ebs_kms_key.id
    volume_size = 8
    volume_type = "gp2"
  }
  tags = {
    Name = "${var.appName}RelMgtInstance"
    Application = var.appName
  }
}

resource "aws_eip" "rlmgt_eip" {
  instance = aws_instance.rlmgt_instance.id
  vpc = true
}

##########################################################################################
# ROUTE 53
##########################################################################################
data "aws_route53_zone" "selected" {
  name = "releasemgt.net."
  private_zone = false
}

resource "aws_route53_record" "rlmgt_dns_record" {
  zone_id = data.aws_route53_zone.selected.zone_id
  name = var.appName
  type = "A"
  ttl = "300"
  records = [aws_eip.rlmgt_eip.public_ip]
}

##########################################################################################
# CODE DEPLOY
##########################################################################################
resource "aws_iam_role" "rlmgt_deployment_role" {
  name = "${var.appName}RelMgtCodeDeployRole"
  description = "Allow CodeDeploy service to access autoscale, ec2 and SNS"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": "codedeploy.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
  tags = {
    Application = var.appName
  }
}

resource "aws_iam_role_policy_attachment" "AWSCodeDeployRole" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSCodeDeployRole"
  role = aws_iam_role.rlmgt_deployment_role.name
}

resource "aws_codedeploy_app" "rlmgt_codedeploy_app" {
  compute_platform = "Server"
  name = "${var.appName}RelMgtApp"
}

resource "aws_codedeploy_deployment_group" "rlmgt_deployment_group" {
  app_name = aws_codedeploy_app.rlmgt_codedeploy_app.name
  deployment_group_name = "${var.appName}RelMgtDeploymentGroup"
  deployment_config_name = "CodeDeployDefault.AllAtOnce"
  service_role_arn = aws_iam_role.rlmgt_deployment_role.arn
  ec2_tag_set {
    ec2_tag_filter {
      key = "Application"
      type = "KEY_AND_VALUE"
      value = var.appName
    }
  }
}

##########################################################################################
# CLOUD WATCH
##########################################################################################
resource "aws_cloudwatch_log_group" "rlmgt_cloudwatch_log_group" {
  name = "${var.appName}RelMgtLogsGroup" #Name must match with variable "logGroupName" defined above
  retention_in_days = 60
  tags = {
    Application = var.appName
  }
}

##########################################################################################
# STORAGE:
#   - zip for code deploy in "releases/" folder
##########################################################################################
resource "aws_s3_bucket" "rlmgt_storage" {
  bucket = "${var.appName}-releasemgt"
  acl = "private"
  tags = {
    Name = "${var.appName}RelMgtStorage"
    Application = var.appName
  }
}

resource "aws_s3_bucket_public_access_block" "rlmgt_storage_access" {
  bucket = aws_s3_bucket.rlmgt_storage.id
  block_public_acls = true
  block_public_policy = true
  ignore_public_acls = true
  restrict_public_buckets = true
}
