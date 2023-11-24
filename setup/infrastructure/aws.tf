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
      source = "hashicorp/aws"
      version = "~> 5.0"
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
  assign_generated_ipv6_cidr_block = true
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
  route {
    ipv6_cidr_block = "::/0"
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
  cidr_block = "10.0.${count.index}.0/24" #2^(32-24)=256 IPs
  ipv6_cidr_block = cidrsubnet(aws_vpc.vpc.ipv6_cidr_block, 8, count.index)
  map_public_ip_on_launch = true
  assign_ipv6_address_on_creation = true
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
  ingress { #port for HTTPS in IPv6 (website request)
    rule_no = 101
    from_port = 443
    to_port = 443
    action = "allow"
    protocol = "tcp"
    ipv6_cidr_block = "::/0"
  }
  ingress { #port for HTTP (website request)
    rule_no = 110
    from_port = 80
    to_port = 80
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  ingress { #port for HTTP in IPv6 (website request)
    rule_no = 111
    from_port = 80
    to_port = 80
    action = "allow"
    protocol = "tcp"
    ipv6_cidr_block = "::/0"
  }
  ingress { #port for SSH (request)
    rule_no = 120
    from_port = 22
    to_port = 22
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  ingress { #port for SSH in IPv6 (request)
    rule_no = 121
    from_port = 22
    to_port = 22
    action = "allow"
    protocol = "tcp"
    ipv6_cidr_block = "::/0"
  }
  ingress { #ephemeral ports for HTTP (EC2 internet response) and HTTPS (S3 response)
    rule_no = 130
    from_port = 1024
    to_port = 65535
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  ingress { #ephemeral ports for HTTP (EC2 internet response) and HTTPS (S3 response) in IPv6
    rule_no = 131
    from_port = 1024
    to_port = 65535
    action = "allow"
    protocol = "tcp"
    ipv6_cidr_block = "::/0"
  }
  ingress { #ping IPv4
    rule_no = 140
    from_port = 0
    to_port = 0
    action = "allow"
    protocol = "icmp"
    cidr_block = "0.0.0.0/0"
    icmp_code = -1
    icmp_type = -1
  }
  egress { #ephemeral ports for SSH (response) and HTTP/HTTPS (website response)
    rule_no = 100
    from_port = 1024
    to_port = 65535
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  egress { #ephemeral ports for SSH (response) and HTTP/HTTPS (website response) in IPv6
    rule_no = 101
    from_port = 1024
    to_port = 65535
    action = "allow"
    protocol = "tcp"
    ipv6_cidr_block = "::/0"
  }
  egress { #port for HTTPS (S3 request)
    rule_no = 110
    from_port = 443
    to_port = 443
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  egress { #port for HTTPS in IPv6 (S3 request)
    rule_no = 111
    from_port = 443
    to_port = 443
    action = "allow"
    protocol = "tcp"
    ipv6_cidr_block = "::/0"
  }
  egress { #port for HTTP (EC2 internet request)
    rule_no = 120
    from_port = 80
    to_port = 80
    action = "allow"
    protocol = "tcp"
    cidr_block = "0.0.0.0/0"
  }
  egress { #port for HTTP in IPv6 (EC2 internet request)
    rule_no = 121
    from_port = 80
    to_port = 80
    action = "allow"
    protocol = "tcp"
    ipv6_cidr_block = "::/0"
  }
  egress { #ping IPv4
    rule_no = 130
    from_port = 0
    to_port = 0
    action = "allow"
    protocol = "icmp"
    cidr_block = "0.0.0.0/0"
    icmp_code = -1
    icmp_type = -1
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
  owners = ["self"]
  filter {
    name = "image-id"
    values = ["ami-055cb71dff2d966de"] #Private AMI with Ubuntu 22.04 LTS
  }
}

resource "aws_iam_role" "instance_role" {
  name = "${var.appName}InstanceRole"
  description = "Role used inside the EC2 instance"
  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Principal": {
          "Service": "ec2.amazonaws.com"
        },
        "Effect": "Allow"
      }
    ]
  })
  tags = {
    Application = var.appName
  }
}

resource "aws_iam_role_policy" "ec2_instance_role_policy" {
  name = "${var.appName}InstanceRolePolicy"
  role = aws_iam_role.instance_role.name
  policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      { #EC2 instance can read system manager parameter store
        "Effect": "Allow",
        "Action": "ssm:GetParameter",
        "Resource": "arn:aws:ssm:*:*:parameter/${var.appName}*"
      },
      { #EC2 instance can request Route53 (let's encrypt)
        "Effect": "Allow",
        "Action": ["route53:ListHostedZones", "route53:GetChange"],
        "Resource": "*"
      },
      { #EC2 instance can add records in Route53 (let's encrypt)
        "Effect": "Allow",
        "Action": ["route53:ChangeResourceRecordSets"],
        "Resource": "arn:aws:route53:::hostedzone/${data.aws_route53_zone.route53_zone_queried.zone_id}"
      },
      { #EC2 instance can login into ECR
        "Effect": "Allow",
        "Action": ["ecr:GetAuthorizationToken"],
        "Resource": "*"
      },
      { #EC2 instance can pull, list and remove images from ECR
        "Effect": "Allow",
        "Action": ["ecr:BatchGetImage", "ecr:GetDownloadUrlForLayer", "ecr:DescribeImages", "ecr:ListImages", "ecr:BatchDeleteImage"],
        "Resource": aws_ecr_repository.docker_registry.arn
      }
    ]
  })
}

resource "aws_iam_instance_profile" "instance_profile" {
  name = "${var.appName}InstanceProfile"
  role = aws_iam_role.instance_role.name
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
    ipv6_cidr_blocks = ["::/0"]
    description = "SSH"
  }
  ingress {
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    description = "HTTPS requests from Internet"
  }
  ingress {
    from_port = 8
    to_port = 0
    protocol = "icmp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Allow ping IPv4"
  }
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    description = "Connection from EC2 to Internet"
  }
  tags = {
    Name = "${var.appName}InstanceSG"
    Application = var.appName
  }
}

resource "aws_network_interface" "network_interface" {
  subnet_id = aws_subnet.public_subnet[0].id
  private_ips = [cidrhost(aws_subnet.public_subnet[0].cidr_block, 11)]
  ipv6_addresses = [cidrhost(aws_subnet.public_subnet[0].ipv6_cidr_block, 16)] #16 (decimal) is displayed as 10 (hexadecimal) in IPv6 address
  security_groups = [aws_security_group.instance_sg.id]
  source_dest_check = false
}

resource "aws_instance" "instance" {
  ami = data.aws_ami.ubuntu.id
  instance_type = "t3a.micro"
  key_name = var.appName
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  network_interface {
    device_index = 0
    network_interface_id = aws_network_interface.network_interface.id
  }
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
    maxRequestsBodySizeInKB = 250
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
  size = 4 #when update value: update also the mount section in 'instancesSetupScript.tmpl.sh'
  type = "gp2"
  multi_attach_enabled = false
  tags = {
    Application = var.appName
  }
}

resource "aws_volume_attachment" "volume_attachment" {
  device_name = "/dev/sda2"
  volume_id = aws_ebs_volume.ebs_volume.id
  instance_id = aws_instance.instance.id
  force_detach = true
}

resource "aws_eip" "eip" {
  instance = aws_instance.instance.id
  domain = "vpc"
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

resource "aws_route53_record" "dns_record_ipv6_backend" {
  zone_id = data.aws_route53_zone.route53_zone_queried.zone_id
  name = "backend"
  type = "AAAA"
  ttl = "60"
  records = aws_network_interface.network_interface.ipv6_addresses
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
      type = "AWS"
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

resource "aws_s3_bucket_public_access_block" "storage_access_frontend" {
  bucket = aws_s3_bucket.storage_frontend.id
  block_public_acls = false
  block_public_policy = false
  ignore_public_acls = false
  restrict_public_buckets = false
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
    origin_id = "website"
  }
  enabled = true
  is_ipv6_enabled = true
  http_version = "http2and3"
  comment = "Managed by Terraform"
  default_root_object = "index.html"
  aliases = [var.domainName, "www.${var.domainName}"]
  default_cache_behavior {
    cache_policy_id = "658327ea-f89d-4fab-a63d-7e88639e58f6" #ID of CachingOptimized (https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/using-managed-cache-policies.html)
    allowed_methods = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods = ["GET", "HEAD"]
    target_origin_id = "website"
    viewer_protocol_policy = "redirect-to-https"
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

resource "aws_route53_record" "dns_record_ipv6_front" {
  zone_id = data.aws_route53_zone.route53_zone_queried.zone_id
  name = ""
  type = "AAAA"
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

resource "aws_route53_record" "dns_record_www_ipv6_front" {
  zone_id = data.aws_route53_zone.route53_zone_queried.zone_id
  name = "www"
  type = "AAAA"
  alias {
    name = aws_cloudfront_distribution.s3_distribution.domain_name
    zone_id = aws_cloudfront_distribution.s3_distribution.hosted_zone_id
    evaluate_target_health = true
  }
}

##########################################################################################
# DEPLOYMENT FROM GITHUB ACTION
##########################################################################################
resource "aws_ecr_repository" "docker_registry" {
  name = var.appName
  image_tag_mutability = "IMMUTABLE"
  image_scanning_configuration {
    scan_on_push = false
  }
}

#Only one OIDC for that url is allowed in an AWS account. DeerVision project is responsible to create it. Others projets only use it !
resource "aws_iam_openid_connect_provider" "git_hub_action_provider" {
  url = "https://token.actions.githubusercontent.com"
  client_id_list = ["sts.amazonaws.com"]
  thumbprint_list = ["6938fd4d98bab03faadb97b34396831e3780aea1"]
}

resource "aws_iam_role" "git_hub_action_role" {
  name = "${var.appName}GitHubAction"
  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Federated": aws_iam_openid_connect_provider.git_hub_action_provider.arn
        },
        "Action": "sts:AssumeRoleWithWebIdentity",
        "Condition": {
          "StringEquals": {"token.actions.githubusercontent.com:aud": "sts.amazonaws.com"},
          "StringLike": {"token.actions.githubusercontent.com:sub": "repo:petitg1987/*"}
        }
      }
    ]
  })
}

resource "aws_iam_role_policy" "git_hub_action_role_policy" {
  name = "${var.appName}GitHubActionRolePolicy"
  role = aws_iam_role.git_hub_action_role.name
  policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      { #GitHub actions can login into ECR
        "Effect": "Allow",
        "Action": ["ecr:GetAuthorizationToken"],
        "Resource": "*"
      },
      { #GitHub actions can push images on ECR
        "Effect": "Allow",
        "Action": ["ecr:BatchCheckLayerAvailability", "ecr:InitiateLayerUpload", "ecr:UploadLayerPart", "ecr:CompleteLayerUpload", "ecr:PutImage"],
        "Resource": aws_ecr_repository.docker_registry.arn
      },
      { #GitHub actions can get EC2 public IP
        "Effect": "Allow",
        "Action": ["ec2:DescribeInstances"],
        "Resource": "*"
      },
      { #GitHub actions can push data in S3
        "Effect": "Allow",
        "Action": ["s3:PutObject", "s3:DeleteObject"],
        "Resource": "${aws_s3_bucket.storage_frontend.arn}/*"
      },
      { #GitHub actions can invalidate cloud front
        "Effect": "Allow",
        "Action": ["cloudfront:ListDistributions", "cloudfront:CreateInvalidation"],
        "Resource": "*"
      }
    ]
  })
}
