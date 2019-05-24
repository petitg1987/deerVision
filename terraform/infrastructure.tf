##########################################################################################
# PRE-REQUISITE
##########################################################################################
# 1) Create IAM User with programmatic access & "AdministratorAccess" IAM policy
# 2) Add keys in ~/.aws/credentials under "[releasemgt]" profile
# 3) Use: terraform init && terraform apply -auto-approve

##########################################################################################
# CREDENTIAL
##########################################################################################
provider "aws" {
  region = "eu-west-3"
  shared_credentials_file = "~/.aws/credentials"
  profile = "releasemgt"
}

##########################################################################################
# NETWORK & ACCESS
##########################################################################################
resource "aws_vpc" "release_mgt_vpc" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "releaseMgtVPC"
  }
}

resource "aws_internet_gateway" "release_mgt_igw" {
  vpc_id = "${aws_vpc.release_mgt_vpc.id}"
  tags = {
    Name = "releaseMgtIGW"
  }
}

resource "aws_route_table" "release_mgt_route" {
  vpc_id = "${aws_vpc.release_mgt_vpc.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.release_mgt_igw.id}"
  }
  tags = {
    Name = "releaseMgtRoute"
  }
}

resource "aws_subnet" "release_mgt_subnet" {
  vpc_id = "${aws_vpc.release_mgt_vpc.id}"
  cidr_block = "10.0.0.0/24"
  map_public_ip_on_launch = true
}

resource "aws_route_table_association" "a" {
  subnet_id = "${aws_subnet.release_mgt_subnet.id}"
  route_table_id = "${aws_route_table.release_mgt_route.id}"
}

resource "aws_security_group" "release_mgt_sg" {
  name = "releaseMgtSG"
  description = "Release Mgt Security Group"
  vpc_id = "${aws_vpc.release_mgt_vpc.id}"
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
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTP"
  }
  ingress { #TODO remove (use SSL until ELB) ?
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTPS"
  }
  tags = {
    Name = "releaseMgtSG"
  }
}

##########################################################################################
# LOAD BALANCER
##########################################################################################
resource "aws_lb_target_group" "release_mgt_elb_target_group" {
  name = "tf-example-lb-tg"
  port= 443
  protocol = "HTTPS"
  vpc_id = "${aws_vpc.release_mgt_vpc.id}"
  health_check {
    enabled = false #TODO active ELB health check
  }
}

resource "aws_lb" "release_mgt_elb" {
  name               = "test-lb-tf"
  internal           = false
  load_balancer_type = "application"
  security_groups    = ["${aws_security_group.release_mgt_sg.id}"]
  subnets            = ["${aws_subnet.release_mgt_subnet.id}"]

  enable_deletion_protection = true

#TODO  access_logs {
#    bucket  = "${aws_s3_bucket.lb_logs.bucket}"
#    prefix  = "test-lb"
#    enabled = true
#  }
}

resource "aws_lb_target_group_attachment" "test" {
  target_group_arn = "${aws_lb_target_group.release_mgt_elb_target_group.arn}"
  target_id        = "${aws_instance.test.id}"
  port             = 80
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

resource "aws_launch_template" "release_mgt_launch_template" {
  name_prefix = "releaseMgtInstance"
  image_id = "${data.aws_ami.ubuntu.id}"
  instance_type = "t2.micro"
  key_name = "releasemgt"
  vpc_security_group_ids = ["${aws_security_group.release_mgt_sg.id}"]
}

resource "aws_autoscaling_group" "release_mgt_asg" {
  desired_capacity = 1
  max_size = 1
  min_size = 1
  vpc_zone_identifier = ["${aws_subnet.release_mgt_subnet.id}"]
  health_check_grace_period = 300
  health_check_type = "ELB"
  tag {
    key = "Name"
    value = "releaseMgtInstance"
    propagate_at_launch = true
  }
  launch_template {
    id = "${aws_launch_template.release_mgt_launch_template.id}"
    version = "$Latest"
  }
}
