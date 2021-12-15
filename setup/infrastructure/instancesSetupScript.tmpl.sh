#!/bin/bash -x

#Install required software
cd /home/ubuntu/ || exit
sudo apt update
sudo add-apt-repository -y ppa:certbot/certbot
sudo apt install -y certbot python3-certbot-dns-route53 nginx ruby wget nfs-common cron openjdk-17-jre

#Mount (and format) volume
sudo mkdir ./data
checkVolume=$(sudo file -s /dev/xvdb)
if [[ "$checkVolume" == "/dev/xvdb: data" ]]; then
  #Volume is empty: format to ext4
  sudo mkfs -t ext4 /dev/xvdb
fi
sudo mount /dev/xvdb /home/ubuntu/data
sudo chown ubuntu:ubuntu -R ./data

#Create certificate (Let's encrypt)
mkdir -p /home/ubuntu/data/letsencrypt/logs
mkdir -p /home/ubuntu/data/letsencrypt/work
mkdir -p /home/ubuntu/data/letsencrypt/config
if [ ! -d "/home/ubuntu/data/letsencrypt/config/live/backend.deervision.studio" ]; then
  sudo certbot -n --agree-tos -m petitg1987@gmail.com --logs-dir /home/ubuntu/data/letsencrypt/logs --work-dir /home/ubuntu/data/letsencrypt/work --config-dir /home/ubuntu/data/letsencrypt/config certonly --dns-route53 -d backend.deervision.studio
fi

#Schedule renew certificate (Let's encrypt)
echo -e "#"'!'"/bin/bash\n\ncertbot --logs-dir /home/ubuntu/data/letsencrypt/logs --work-dir /home/ubuntu/data/letsencrypt/work --config-dir /home/ubuntu/data/letsencrypt/config renew --dns-route53\nsudo service nginx reload" | sudo tee /etc/cron.daily/deervision-renew-cert
sudo chmod +x /etc/cron.daily/deervision-renew-cert

#Setup Nginx
echo -e "limit_req_zone \$binary_remote_addr zone=req_zone:500m rate=${maxRequestsBySecond}r/s;\n" \
  "server {\n" \
  "  listen 443;\n" \
  "  server_name _;\n" \
  "  ssl on;\n" \
  "  ssl_certificate /home/ubuntu/data/letsencrypt/config/live/backend.deervision.studio/fullchain.pem;\n" \
  "  ssl_certificate_key /home/ubuntu/data/letsencrypt/config/live/backend.deervision.studio/privkey.pem;\n" \
  "  client_max_body_size ${maxRequestsBodySizeInKB}K;\n" \
  "  location / {\n"\
  "    limit_req zone=req_zone burst=${maxRequestsBurst} nodelay;\n" \
  "    proxy_set_header X-Forwarded-Host \$host;\n" \
  "    proxy_set_header X-Forwarded-Port 443;\n" \
  "    proxy_set_header X-Forwarded-Proto https;\n" \
  "    proxy_pass http://127.0.0.1:8080;\n" \
  "  }\n" \
  "}" | sudo tee /etc/nginx/sites-available/reverseproxy
sudo rm /etc/nginx/sites-enabled/default
sudo ln -s /etc/nginx/sites-available/reverseproxy /etc/nginx/sites-enabled/reverseproxy
sudo systemctl restart nginx

#Setup CodeDeploy agent
wget https://aws-codedeploy-eu-central-1.s3.eu-central-1.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
rm ./install
sudo service codedeploy-agent start

#Setup CloudWatch agent
# - Note 1: JSON configuration logs file is /opt/aws/amazon-cloudwatch-agent/logs/configuration-validation.log
# - Note 2: execution logs file is /opt/aws/amazon-cloudwatch-agent/logs/amazon-cloudwatch-agent.log
wget https://s3.amazonaws.com/amazoncloudwatch-agent/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i -E ./amazon-cloudwatch-agent.deb
cat <<EOT > ./amazon-cloudwatch-agent.json
{
  "agent": {
    "metrics_collection_interval": 60
  },

  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/home/ubuntu/application.log",
            "log_group_name": "${logGroupName}",
            "log_stream_name": "${logStreamNamePrefix}-{instance_id}",
            "timezone": "Local"
          }
        ]
      }
    },
    "log_stream_name": "${logStreamNamePrefix}Default",
    "force_flush_interval" : 15
  },

  "metrics": {
    "metrics_collected": {
      "disk": {
        "resources": [
          "/"
        ],
        "measurement": [
          "free",
          "total",
          "used"
        ],
        "metrics_collection_interval": 60
      },
      "mem": {
        "measurement": [
          "mem_used",
          "mem_cached",
          "mem_total"
        ],
        "metrics_collection_interval": 60
      }
    }
  }
}
EOT
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ubuntu/amazon-cloudwatch-agent.json -s
sudo rm amazon-cloudwatch-agent.deb
