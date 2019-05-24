provider "aws" {
  region = "eu-west-3"
  shared_credentials_file = "~/.aws/credentials"
  profile = "releasemgt"
}

##########################################################################################
# NETWORK
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
  subnet_id      = "${aws_subnet.release_mgt_subnet.id}"
  route_table_id = "${aws_route_table.release_mgt_route.id}"
}

resource "aws_security_group" "release_mgt_sg" {
  name        = "releaseMgtSG"
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
  ingress { #TODO remove ?
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTP"
  }
  ingress {
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
# INSTANCES
##########################################################################################
data "aws_ami" "ubuntu" {
  most_recent = true
  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-bionic-18.04-amd64-server-*"]
  }
  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
  owners = ["099720109477"]
}

resource "aws_instance" "release_mgt_instance" {
  ami = "${data.aws_ami.ubuntu.id}"
  instance_type = "t2.micro"
  subnet_id = "${aws_subnet.release_mgt_subnet.id}"
  security_groups = ["${aws_security_group.release_mgt_sg.id}"]
  key_name = "releasemgt"
  tags = {
    Name = "releaseMgtInstance"
  }
}
