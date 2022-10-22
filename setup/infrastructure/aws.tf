##########################################################################################
# VARIABLES
##########################################################################################
variable "appName" {
  description = "Application name"
  type = string
}

variable "domainName" {
  description = "Domain name"
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
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region = var.region
  shared_credentials_files = ["~/.aws/credentials"]
  profile = "deervision"
}

provider "aws" {
  alias = "virgina"
  region = "us-east-1"
}

##########################################################################################
# NETWORK & ACCESS
##########################################################################################
resource "aws_vpc" "vpc" {
  cidr_block = "10.0.0.0/16"
  enable_dns_hostnames = true
  tags = {
    Name = "${var.appName}VPC"
    Application = var.appName
  }
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc.id
  tags = {
    Name = "${var.appName}IGW"
    Application = var.appName
  }
}

resource "aws_route_table" "route" {
  vpc_id = aws_vpc.vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = {
    Name = "${var.appName}Route"
    Application = var.appName
  }
}

resource "aws_subnet" "public_subnet" {
  count = length(var.availabilityZones)
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.${count.index}.0/24"
  map_public_ip_on_launch = true
  availability_zone = var.availabilityZones[count.index]
  tags = {
    Application = var.appName
  }
}

resource "aws_route_table_association" "route_association" {
  count = length(var.availabilityZones)
  subnet_id = aws_subnet.public_subnet[count.index].id
  route_table_id = aws_route_table.route.id
}

resource "aws_network_acl" "network_acl" {
  vpc_id = aws_vpc.vpc.id
  subnet_ids = aws_subnet.public_subnet.*.id
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
    Name = "${var.appName}NetworkACL"
    Application = var.appName
  }
}

##########################################################################################
# INSTANCES
##########################################################################################
data "aws_ami" "ubuntu" {
  most_recent = true
  owners = ["amazon"]
  filter {
    name = "image-id"
    values = ["ami-0caef02b518350c8b"] #Ubuntu Server 22.04 LTS (HVM), SSD Volume Type
  }
}

resource "aws_iam_role" "instance_role" {
  name = "${var.appName}InstanceRole"
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

resource "aws_security_group" "instance_sg" {
  name = "${var.appName}InstanceSG"
  description = "Deer Vision Instance Security Group"
  vpc_id = aws_vpc.vpc.id
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
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Connection from EC2 to Internet"
  }
  tags = {
    Name = "${var.appName}InstanceSG"
    Application = var.appName
  }
}

resource "aws_iam_role_policy_attachment" "AmazonEC2RoleforAWSCodeDeploy" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforAWSCodeDeploy"
  role = aws_iam_role.instance_role.name
}

resource "aws_iam_role_policy_attachment" "AutoScalingNotificationAccessRole" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AutoScalingNotificationAccessRole"
  role = aws_iam_role.instance_role.name
}

resource "aws_iam_role_policy_attachment" "CloudWatchAgentAdminPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentAdminPolicy"
  role = aws_iam_role.instance_role.name
}

resource "aws_iam_role_policy_attachment" "AmazonRoute53FullAccess" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonRoute53FullAccess"
  role = aws_iam_role.instance_role.name
}

resource "aws_iam_instance_profile" "instance_profile" {
  name = "${var.appName}InstanceProfile"
  role = aws_iam_role.instance_role.name
}

data "aws_iam_role" "AWSServiceRoleForAutoScaling" {
  name = "AWSServiceRoleForAutoScaling"
}

resource "aws_instance" "instance" {
  ami = data.aws_ami.ubuntu.id
  instance_type = "t2.micro"
  key_name = "deervision"
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  subnet_id = aws_subnet.public_subnet[0].id
  vpc_security_group_ids = [aws_security_group.instance_sg.id]
  disable_api_termination = false
  ebs_optimized = false
  hibernation = false
  monitoring = false
  credit_specification {
    cpu_credits = "standard"
  }
  user_data = base64encode(templatefile("${path.module}/instancesSetupScript.tmpl.sh", {
    maxRequestsBySecond = 10,
    maxRequestsBurst = 20,
    maxRequestsBodySizeInKB = 250,
    logGroupName = "${var.appName}LogsGroup",
    logStreamNamePrefix = "${var.appName}LogsStream"
  }))
  root_block_device { //volume for the OS (destroy with instance termination)
    delete_on_termination = "true"
    encrypted = "false"
    volume_size = 8
    volume_type = "gp2"
  }
  tags = {
    Name = "${var.appName}Instance"
    Application = var.appName
  }
}

resource "aws_ebs_volume" "ebs_volume" { //volume for the database (not destroyed on instance termination)
  availability_zone = aws_subnet.public_subnet[0].availability_zone
  encrypted = "false"
  size = 4
  type = "gp2"
  multi_attach_enabled = false
  tags = {
    Application = var.appName
  }
}

resource "aws_volume_attachment" "volume_attachment" {
  device_name = "/dev/sda2"
  volume_id   = aws_ebs_volume.ebs_volume.id
  instance_id = aws_instance.instance.id
  force_detach = true
}

resource "aws_eip" "eip" {
  instance = aws_instance.instance.id
  vpc = true
}

##########################################################################################
# ROUTE 53
##########################################################################################
data "aws_route53_zone" "route53_zone_queried" {
  name = "${var.domainName}."
  private_zone = false
}

resource "aws_route53_record" "dns_record_backend" {
  zone_id = data.aws_route53_zone.route53_zone_queried.zone_id
  name = "backend"
  type = "A"
  ttl = "60"
  records = [aws_eip.eip.public_ip]
}

##########################################################################################
# CLOUD WATCH
##########################################################################################
resource "aws_cloudwatch_log_group" "cloudwatch_log_group" {
  name = "${var.appName}LogsGroup" #Name must match with variable "logGroupName" defined above
  retention_in_days = 60
  tags = {
    Application = var.appName
  }
}

##########################################################################################
# CODE DEPLOY
##########################################################################################
resource "aws_iam_role" "deployment_role" {
  name = "${var.appName}CodeDeployRole"
  description = "Allow CodeDeploy service to access autoscale, EC2 and SNS"
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
  role = aws_iam_role.deployment_role.name
}

resource "aws_codedeploy_app" "codedeploy_app" {
  compute_platform = "Server"
  name = "${var.appName}App"
}

resource "aws_codedeploy_deployment_group" "deployment_group" {
  app_name = aws_codedeploy_app.codedeploy_app.name
  deployment_group_name = "${var.appName}DeploymentGroup"
  deployment_config_name = "CodeDeployDefault.AllAtOnce"
  service_role_arn = aws_iam_role.deployment_role.arn
  ec2_tag_set {
    ec2_tag_filter {
      key = "Application"
      type = "KEY_AND_VALUE"
      value = var.appName
    }
  }
}

resource "aws_s3_bucket" "storage_backend" {
  bucket = "${var.appName}-backend"
  tags = {
    Name = "${var.appName}Backend"
    Application = var.appName
  }
}

resource "aws_s3_bucket_acl" "storage_backend_bucket_acl" {
  bucket = aws_s3_bucket.storage_backend.id
  acl = "private"
}

resource "aws_s3_bucket_public_access_block" "storage_access_backend" {
  bucket = aws_s3_bucket.storage_backend.id
  block_public_acls = true
  block_public_policy = true
  ignore_public_acls = true
  restrict_public_buckets = true
}

##########################################################################################
# STATIC FRONTEND ON S3
##########################################################################################
resource "aws_s3_bucket" "storage_frontend" {
  bucket = "${var.appName}-frontend"
  tags = {
    Name = "${var.appName}Frontend"
  }
}

resource "aws_s3_bucket_policy" "storage_frontend_policy" {
  bucket = aws_s3_bucket.storage_frontend.id
  policy = data.aws_iam_policy_document.storage_frontend_policy_document.json
}

data "aws_iam_policy_document" "storage_frontend_policy_document" {
  statement {
    sid = "PublicReadForGetBucketObjects"

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    actions = [
      "s3:GetObject",
    ]

    resources = [
      aws_s3_bucket.storage_frontend.arn,
      "${aws_s3_bucket.storage_frontend.arn}/*",
    ]
  }
}

resource "aws_s3_bucket_acl" "storage_frontend_bucket_acl" {
  bucket = aws_s3_bucket.storage_frontend.id
  acl = "public-read"
}

resource "aws_s3_bucket_website_configuration" "storage_frontend_website" {
  bucket = aws_s3_bucket.storage_frontend.bucket

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "error404.html"
  }
}

resource "aws_s3_bucket_cors_configuration" "storage_frontend_cors" {
  bucket = aws_s3_bucket.storage_frontend.id

  cors_rule {
    allowed_headers = ["*"]
    allowed_methods = ["GET", "POST"]
    allowed_origins = ["*"]
    expose_headers = ["ETag"]
    max_age_seconds = 3000
  }
}

data "aws_acm_certificate" "acm_certificate" {
  domain = var.domainName
  statuses = ["ISSUED"]
  provider = aws.virgina
}

resource "aws_cloudfront_distribution" "s3_distribution" {
  origin {
    domain_name = aws_s3_bucket.storage_frontend.bucket_regional_domain_name
    origin_id   = "website"
  }
  enabled = true
  is_ipv6_enabled = true
  http_version = "http2and3"
  comment = "Managed by Terraform"
  default_root_object = "index.html"
  aliases = [var.domainName, "www.${var.domainName}"]
  default_cache_behavior {
    allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "website"
    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }
    viewer_protocol_policy = "redirect-to-https"
    min_ttl = 0
    default_ttl = 3600
    max_ttl = 86400
    compress = true
  }
  price_class = "PriceClass_100"
  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }
  custom_error_response {
    error_code = 404
    response_page_path = "/index.html"
    response_code = 200
  }
  viewer_certificate {
    acm_certificate_arn = data.aws_acm_certificate.acm_certificate.arn
    minimum_protocol_version = "TLSv1.2_2019"
    ssl_support_method = "sni-only"
  }
}

resource "aws_route53_record" "dns_record_front" {
  zone_id = data.aws_route53_zone.route53_zone_queried.zone_id
  name = ""
  type = "A"
  alias {
    name = aws_cloudfront_distribution.s3_distribution.domain_name
    zone_id = aws_cloudfront_distribution.s3_distribution.hosted_zone_id
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "dns_record_www_front" {
  zone_id = data.aws_route53_zone.route53_zone_queried.zone_id
  name = "www"
  type = "A"
  alias {
    name = aws_cloudfront_distribution.s3_distribution.domain_name
    zone_id = aws_cloudfront_distribution.s3_distribution.hosted_zone_id
    evaluate_target_health = true
  }
}

##########################################################################################
# CONTACT EMAIL USING SNS
##########################################################################################
resource "aws_ses_email_identity" "email_registration" {
  email = "contact@${var.domainName}"
  provider = aws.virgina
}

resource "aws_ses_receipt_rule_set" "contact_rule_set" {
  rule_set_name = "contact-receipt-rules"
  provider = aws.virgina
}

#Only one active rule set is allow for an AWS account. DeerVision project is responsible to create it. Others projets only use it !
resource "aws_ses_active_receipt_rule_set" "contact_active_rule_set" {
  rule_set_name = aws_ses_receipt_rule_set.contact_rule_set.rule_set_name
  provider = aws.virgina
}

data "aws_sns_topic" "contact_topic" {
  name = "contact-email-topic-${var.appName}"
  provider = aws.virgina
}

resource "aws_ses_receipt_rule" "receipt_email_to_sns" {
  name = "receipt_email_to_sns-${var.appName}"
  rule_set_name = aws_ses_active_receipt_rule_set.contact_active_rule_set.rule_set_name
  recipients = ["contact@${var.domainName}"]
  enabled = true
  scan_enabled = true
  sns_action {
    topic_arn = data.aws_sns_topic.contact_topic.arn
    encoding = "UTF-8"
    position = 1
  }
  provider = aws.virgina
}

resource "aws_route53_record" "dns_mx_record" {
  zone_id = data.aws_route53_zone.route53_zone_queried.zone_id
  name = var.domainName
  type = "MX"
  ttl = "60"
  records = ["10 inbound-smtp.us-east-1.amazonaws.com"]
}
