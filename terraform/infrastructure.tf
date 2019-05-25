##########################################################################################
# PRE-REQUISITE
##########################################################################################
# - Create IAM User named "release-mgt-user" with programmatic access and having "AdministratorAccess" IAM policy
# - Add IAM user keys in ~/.aws/credentials under "[releasemgt]" profile
# - Create EC2 key pair named "releasemgt" and add them in ~/.ssh/
# - Delete the default VPC (optional but more clean)
# - Create DNS "releasemgt.net" by using Route 53
# - Request a public certificate for following domains on Amazon Certificate Manager: releasemgt.net & *.releasemgt.net
# - Use:
#     - terraform init
#     - terraform apply -auto-approve
#     - terraform destroy

##########################################################################################
# CREDENTIAL & LOCATION
##########################################################################################
provider "aws" {
  region = "eu-west-3"
  shared_credentials_file = "~/.aws/credentials"
  profile = "releasemgt"
}

variable "availabilityZones" {
  description = "Run the EC2 Instances in these Availability Zones"
  type = "list"
  default = ["eu-west-3a", "eu-west-3b", "eu-west-3c"]
}

##########################################################################################
# NETWORK & ACCESS
##########################################################################################
resource "aws_vpc" "rlmgt_vpc" {
  cidr_block = "10.0.0.0/16"
  enable_dns_hostnames = true
  tags = {
    Name = "releaseMgtVPC"
  }
}

resource "aws_internet_gateway" "rlmgt_igw" {
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  tags = {
    Name = "releaseMgtIGW"
  }
}

resource "aws_route_table" "rlmgt_route" {
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.rlmgt_igw.id}"
  }
  tags = {
    Name = "releaseMgtRoute"
  }
}

resource "aws_subnet" "rlmgt_public_subnet" {
  count = "${length(var.availabilityZones)}"
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  cidr_block = "10.0.${count.index}.0/24"
  map_public_ip_on_launch = true
  availability_zone = "${element(var.availabilityZones, count.index)}"
}

resource "aws_route_table_association" "rlmgt_route_association" {
  count = "${length(var.availabilityZones)}"
  subnet_id = "${element(aws_subnet.rlmgt_public_subnet, count.index).id}"
  route_table_id = "${aws_route_table.rlmgt_route.id}"
}

resource "aws_security_group" "rlmgt_instance_sg" {
  name = "releaseMgtInstanceSG"
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
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Connection from EC2 to Internet"
  }
  tags = {
    Name = "releaseMgtInstanceSG"
  }
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

resource "aws_launch_template" "rlmgt_launch_template" {
  name_prefix = "releaseMgtInstance"
  image_id = "${data.aws_ami.ubuntu.id}"
  instance_type = "t2.micro"
  key_name = "releasemgt"
  vpc_security_group_ids = ["${aws_security_group.rlmgt_instance_sg.id}"]
  user_data = "IyEvYmluL2Jhc2gKc3VkbyBhcHQgaW5zdGFsbCAteSBhcGFjaGUy" #TODO remove
}

resource "aws_autoscaling_group" "rlmgt_asg" {
  desired_capacity = 1
  max_size = 1
  min_size = 1
  vpc_zone_identifier = "${aws_subnet.rlmgt_public_subnet.*.id}"
  health_check_grace_period = 300
  health_check_type = "ELB"
  tag {
    key = "Name"
    value = "releaseMgtInstance"
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
  name = "releaseMgtTargetGroup"
  port= 80
  protocol = "HTTP"
  vpc_id = "${aws_vpc.rlmgt_vpc.id}"
  target_type = "instance"
  health_check {
    protocol = "HTTP"
    port = 80
    path = "/"
  }
}

resource "aws_s3_bucket" "rlmgt_s3_access_logs_bucket" {
  bucket = "rlmgt-elb-access-logs-bucket"
  acl = "private"
  tags = {
    Name = "Release Mgt ELB Access Logs Buket"
  }
}

data "aws_iam_user" "rlmgt_iam_user" {
  user_name = "release-mgt-user"
}

resource "aws_s3_bucket_policy" "rlmgt_s3_policy" {
  bucket = "${aws_s3_bucket.rlmgt_s3_access_logs_bucket.id}"
  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Id": "releaseMgtElbAccessLogsBucketId",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "s3:PutObject",
      "Resource": "arn:aws:s3:::rlmgt-elb-access-logs-bucket/*",
      "Principal": {
        "AWS": ["009996457667"]
      }
    },
    {
      "Effect": "Allow",
      "Action": "s3:*",
      "Resource": "arn:aws:s3:::rlmgt-elb-access-logs-bucket",
      "Principal": {
        "AWS": ["${data.aws_iam_user.rlmgt_iam_user.arn}"]
      }
    }

  ]
}
POLICY
}

resource "aws_security_group" "rlmgt_elb_sg" {
  name = "releaseMgtElbSG"
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
    Name = "releaseMgtElbSG"
  }
}

resource "aws_lb" "rlmgt_elb" {
  name = "releaseMgtElb"
  internal = false
  load_balancer_type = "application"
  security_groups = ["${aws_security_group.rlmgt_elb_sg.id}"]
  subnets = "${aws_subnet.rlmgt_public_subnet.*.id}"
  enable_deletion_protection = false
  access_logs {
    bucket = "${aws_s3_bucket.rlmgt_s3_access_logs_bucket.bucket}"
    prefix = "rlmgt-access-logs"
    enabled = true
  }
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
  name = "greencity" #TODO parameter
  type = "A"
  alias {
    name = "${aws_lb.rlmgt_elb.dns_name}"
    zone_id = "${aws_lb.rlmgt_elb.zone_id}"
    evaluate_target_health = false
  }
}
