##########################################################################################
# VARIABLES
##########################################################################################
variable "appName" {
  description = "Application name"
  type = "string"
}

variable "cidrPrefix" {
  description = "CIDR prefix (e.g.: 10.0)"
  type = "string"
}

variable "region" {
  description = "Run the EC2 instances in this region"
  type = "string"
  default = "eu-central-1"
}

variable "availabilityZones" {
  description = "Run the EC2 instances in these availability zones"
  type = "list"
  default = ["eu-central-1a", "eu-central-1b", "eu-central-1c"]
}

##########################################################################################
# CREDENTIAL & LOCATION
##########################################################################################
provider "aws" {
  region = "${var.region}"
  shared_credentials_file = "~/.aws/credentials"
  profile = "releasemgt"
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
  }
}

resource "aws_internet_gateway" "rlmgt_igw" {
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  tags = {
    Name = "${var.appName}RelMgtIGW"
  }
}

resource "aws_route_table" "rlmgt_route" {
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.rlmgt_igw.id}"
  }
  tags = {
    Name = "${var.appName}RelMgtRoute"
  }
}

resource "aws_subnet" "rlmgt_public_subnet" {
  count = "${length(var.availabilityZones)}"
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  cidr_block = "${var.cidrPrefix}.${count.index}.0/24"
  map_public_ip_on_launch = true
  availability_zone = "${element(var.availabilityZones, count.index)}"
}

resource "aws_route_table_association" "rlmgt_route_association" {
  count = "${length(var.availabilityZones)}"
  subnet_id = "${element(aws_subnet.rlmgt_public_subnet, count.index).id}"
  route_table_id = "${aws_route_table.rlmgt_route.id}"
}

##########################################################################################
# EFS
##########################################################################################
resource "aws_efs_file_system" "rlmgt_efs" {
  creation_token = "${var.appName}RelMgEfsToken"
  tags = {
    Name = "${var.appName}RelMgtEfsName"
  }
}

resource "aws_security_group" "rlmgt_efs_sg" {
  name = "${var.appName}RelMgtEfsSG"
  description = "Release Mgt EFS Security Group"
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  ingress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    security_groups = ["${aws_security_group.rlmgt_instance_sg.id}"]
    description = "Instances can access to EFS"
  }
  tags = {
    Name = "${var.appName}RelMgtInstanceSG"
  }
}

resource "aws_efs_mount_target" "rlmgt_efs_mount_target" {
  count = "${length(var.availabilityZones)}"
  file_system_id = "${aws_efs_file_system.rlmgt_efs.id}"
  subnet_id = "${element(aws_subnet.rlmgt_public_subnet, count.index).id}"
  security_groups = ["${aws_security_group.rlmgt_efs_sg.id}"]
}

##########################################################################################
# INSTANCES
##########################################################################################
data "aws_ami" "ubuntu" {
  most_recent = true
  filter {
    name = "name"
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
  description = "Allow EC2 instances to access to S3, SNS and SQS which are used by CodeDeploy agent"
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
}

resource "aws_security_group" "rlmgt_instance_sg" {
  name = "${var.appName}RelMgtInstanceSG"
  description = "Release Mgt Instance Security Group"
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "SSH"
  }
  ingress {
    protocol = "icmp"
    from_port = 8 # ICMP type: echo request
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
    description = "Ping"
  }
  ingress {
    from_port = 80
    to_port = 80
    protocol = "tcp"
    security_groups = ["${aws_security_group.rlmgt_elb_sg.id}"]
    #cidr_blocks = ["0.0.0.0/0"] #Debug: use to access instance without going through ELB
    description = "HTTP requests from ELB"
  }
  ingress {
    from_port = 2049
    to_port = 2049
    protocol = "tcp"
    cidr_blocks = ["${aws_vpc.rlmgt_vpc.cidr_block}"] #Alternative: use EFS security group
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
  }
}

resource "aws_iam_role_policy_attachment" "AmazonEC2RoleforAWSCodeDeploy" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforAWSCodeDeploy"
  role = "${aws_iam_role.rlmgt_instance_role.name}"
}

resource "aws_iam_role_policy_attachment" "AutoScalingNotificationAccessRole" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AutoScalingNotificationAccessRole"
  role = "${aws_iam_role.rlmgt_instance_role.name}"
}

resource "aws_iam_instance_profile" "rlmgt_instance_profile" {
  name = "${var.appName}RelMgtInstanceProfile"
  role = "${aws_iam_role.rlmgt_instance_role.name}"
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
}

resource "aws_kms_alias" "rlmgt_ebs_kms_key_alias" {
  name = "alias/${var.appName}EbsKmsKey"
  target_key_id = "${aws_kms_key.rlmgt_ebs_kms_key.key_id}"
}

resource "aws_launch_template" "rlmgt_launch_template" {
  name_prefix = "${var.appName}RelMgtInstance"
  image_id = "${data.aws_ami.ubuntu.id}"
  instance_type = "t2.micro"
  key_name = "releasemgt"
  iam_instance_profile {
    name = "${aws_iam_instance_profile.rlmgt_instance_profile.name}"
  }
  vpc_security_group_ids = ["${aws_security_group.rlmgt_instance_sg.id}"]
  user_data = "${base64encode(templatefile("${path.module}/instancesSetupScript.tmpl.sh", { efsDnsName = aws_efs_file_system.rlmgt_efs.dns_name }))}"
  block_device_mappings {
    device_name = "/dev/sda1"
    ebs {
      delete_on_termination = "true"
      encrypted = "true"
      kms_key_id = "${aws_kms_key.rlmgt_ebs_kms_key.id}"
      volume_size = 8
    }
  }
}

resource "aws_autoscaling_group" "rlmgt_asg" {
  desired_capacity = 1
  max_size = 1
  min_size = 1
  vpc_zone_identifier = "${aws_subnet.rlmgt_public_subnet.*.id}"
  health_check_grace_period = 300
  health_check_type = "ELB"
  service_linked_role_arn = "${data.aws_iam_role.AWSServiceRoleForAutoScaling.arn}"
  tag {
    key = "Name"
    value = "${var.appName}RelMgtInstance"
    propagate_at_launch = true
  }
  launch_template {
    id = "${aws_launch_template.rlmgt_launch_template.id}"
    version = "$Latest"
  }
}

##########################################################################################
# LOAD BALANCER
##########################################################################################
resource "aws_lb_target_group" "rlmgt_elb_target_group" {
  name = "${var.appName}RelMgtTargetGroup"
  port= 80
  protocol = "HTTP"
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  target_type = "instance"
  health_check {
    protocol = "HTTP"
    port = 80
    path = "/login"
  }
}

resource "aws_security_group" "rlmgt_elb_sg" {
  name = "${var.appName}RelMgtElbSG"
  description = "Release Mgt ELB Security Group"
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  ingress {
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    description = "HTTP"
  }
  ingress {
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    description = "HTTPS"
  }
  ingress {
    protocol = "icmp"
    from_port = 8 # ICMP type: echo request
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
    description = "Ping"
  }
  egress {
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    description = "Instances access (Web page and health check)"
  }
  tags = {
    Name = "${var.appName}RelMgtElbSG"
  }
}

resource "aws_lb" "rlmgt_elb" {
  name = "${var.appName}RelMgtElb"
  internal = false
  load_balancer_type = "application"
  security_groups = ["${aws_security_group.rlmgt_elb_sg.id}"]
  subnets = "${aws_subnet.rlmgt_public_subnet.*.id}"
  enable_deletion_protection = false
}

data "aws_acm_certificate" "rlmgt_domain_certificate" {
  domain = "releasemgt.net"
  types = ["AMAZON_ISSUED"]
  most_recent = true
}

resource "aws_lb_listener" "rlmgt_elb_listener_https" {
  load_balancer_arn = "${aws_lb.rlmgt_elb.arn}"
  port = "443"
  protocol = "HTTPS"
  ssl_policy = "ELBSecurityPolicy-2016-08"
  certificate_arn = "${data.aws_acm_certificate.rlmgt_domain_certificate.arn}"
  default_action {
    type = "forward"
    target_group_arn = "${aws_lb_target_group.rlmgt_elb_target_group.arn}"
  }
}

resource "aws_lb_listener" "rlmgt_elb_listener_http" {
  load_balancer_arn = "${aws_lb.rlmgt_elb.arn}"
  port = "80"
  protocol = "HTTP"
  default_action {
    type = "redirect"
    redirect {
      port = "443"
      protocol = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

resource "aws_autoscaling_attachment" "rlmgt_asg_attachment" {
  autoscaling_group_name = "${aws_autoscaling_group.rlmgt_asg.id}"
  alb_target_group_arn = "${aws_lb_target_group.rlmgt_elb_target_group.arn}"
}

##########################################################################################
# ROUTE 53
##########################################################################################
data "aws_route53_zone" "selected" {
  name = "releasemgt.net."
  private_zone = false
}

resource "aws_route53_record" "rlmgt_dns_record" {
  zone_id = "${data.aws_route53_zone.selected.zone_id}"
  name = "${var.appName}"
  type = "A"
  alias {
    name = "${aws_lb.rlmgt_elb.dns_name}"
    zone_id = "${aws_lb.rlmgt_elb.zone_id}"
    evaluate_target_health = false
  }
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
}

resource "aws_iam_role_policy_attachment" "AWSCodeDeployRole" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSCodeDeployRole"
  role = "${aws_iam_role.rlmgt_deployment_role.name}"
}

resource "aws_codedeploy_app" "rlmgt_codedeploy_app" {
  compute_platform = "Server"
  name = "${var.appName}RelMgtApp"
}

resource "aws_codedeploy_deployment_group" "rlmgt_deployment_group" {
  app_name = "${aws_codedeploy_app.rlmgt_codedeploy_app.name}"
  deployment_group_name = "${var.appName}RelMgtDeploymentGroup"
  deployment_config_name = "CodeDeployDefault.AllAtOnce"
  service_role_arn = "${aws_iam_role.rlmgt_deployment_role.arn}"
  autoscaling_groups = ["${aws_autoscaling_group.rlmgt_asg.id}"]
  load_balancer_info {
    elb_info {
      name = "${aws_lb.rlmgt_elb.name}"
    }
  }
}

##########################################################################################
# STORAGE:
#   - zip for code deploy in "releases/" folder
#   - binaries of the managed application in "binaries/" folder
##########################################################################################
resource "aws_s3_bucket" "rlmgt_storage" {
  bucket = "${var.appName}-releasemgt"
  acl = "private"
  tags = {
    Name = "${var.appName}RelMgtStorage"
  }
}

resource "aws_s3_bucket_public_access_block" "rlmgt_storage_access" {
  bucket = "${aws_s3_bucket.rlmgt_storage.id}"
  block_public_acls = true
  block_public_policy = true
  ignore_public_acls = true
  restrict_public_buckets = true
}
