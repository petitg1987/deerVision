#!/bin/bash -x

# ===============================================================
# /!\ CHANGE IN THIS FILE REQUIRE: ./infra.sh recreateInstance
# ===============================================================

#Create script log file
echo "$(date '+%Y-%m-%d %H:%M:%S') - Start script execution" >> /home/ubuntu/userdata.log

#Mount (and format) volume
echo "$(date '+%Y-%m-%d %H:%M:%S') - Mounting data volume" >> /home/ubuntu/userdata.log
sleep 90 #wait to be sure EBS volume is well attached to the EC2 instance
cd /home/ubuntu/ || exit
sudo mkdir ./data
volumeName="/dev/"$(lsblk | grep 'disk' | grep '4G' | cut -f 1 -d ' ')
echo "$(date '+%Y-%m-%d %H:%M:%S') - Volume name is $volumeName" >> /home/ubuntu/userdata.log
checkVolume=$(sudo file -s $volumeName)
if [[ "$checkVolume" == "$volumeName: data" ]]; then
  echo "$(date '+%Y-%m-%d %H:%M:%S') - Formatting empty data volume in ext4" >> /home/ubuntu/userdata.log
  sudo mkfs -t ext4 $volumeName
fi
echo "$volumeName   /home/ubuntu/data   ext4   defaults   0   0" | sudo tee -a /etc/fstab
sudo mount -a
sudo chown ubuntu:ubuntu -R ./data
checkVolumeMountedResult=$(df -h)
if [[ $checkVolumeMountedResult == *"/home/ubuntu/data"* ]]; then
  echo "$(date '+%Y-%m-%d %H:%M:%S') - Volume is correctly mounted" >> /home/ubuntu/userdata.log
else
  echo "$(date '+%Y-%m-%d %H:%M:%S') - Volume mount fail" >> /home/ubuntu/userdata.log
  exit 1
fi

#Launch database
echo "$(date '+%Y-%m-%d %H:%M:%S') - Launching database" >> /home/ubuntu/userdata.log
dbPassword=$(aws ssm get-parameter --region eu-central-1 --name deervisionDbPassword --query 'Parameter.Value' --output text)
mkdir -p /home/ubuntu/data/db
sudo docker run --restart always --name deervision-db -e POSTGRES_PASSWORD=$dbPassword -v /home/ubuntu/data/db/:/var/lib/postgresql/data -p 5432:5432 -d postgres:14.5
sleep 10 #Wait volume is created by docker
sudo chmod 755 -R /home/ubuntu/data/db

#Schedule database backup
echo "$(date '+%Y-%m-%d %H:%M:%S') - Scheduling database backup" >> /home/ubuntu/userdata.log
echo -e "#"'!'"/bin/bash\n\n" \
  "if [ \"\$(id -u)\" -eq 0 ]; then\n  exec sudo -H -u ubuntu \$0 \"\$@\"\nfi\n\n" \
  "sudo docker exec -t deervision-db pg_dump -U postgres > /home/ubuntu/deervision_dump.sql\n" \
  "zip -r /home/ubuntu/deervision_dump.zip /home/ubuntu/deervision_dump.sql\n" \
  "aws s3 cp --region eu-central-1 /home/ubuntu/deervision_dump.zip s3://deervision-backend/db-backup/deervision_dump_\`date +%Y-%m-%d"_"%H_%M_%S\`.zip\n" \
  "rm /home/ubuntu/deervision_dump.sql /home/ubuntu/deervision_dump.zip" | sudo tee /etc/cron.daily/deervision-db-backup
sudo chmod +x /etc/cron.daily/deervision-db-backup

#Create certificate (Let's encrypt)
mkdir -p /home/ubuntu/data/letsencrypt/logs
mkdir -p /home/ubuntu/data/letsencrypt/work
mkdir -p /home/ubuntu/data/letsencrypt/config
if [ ! -d "/home/ubuntu/data/letsencrypt/config/live/backend.deervision.studio" ]; then
  echo "$(date '+%Y-%m-%d %H:%M:%S') - Creating certificate" >> /home/ubuntu/userdata.log
  sudo certbot -n --agree-tos -m petitg1987@gmail.com --logs-dir /home/ubuntu/data/letsencrypt/logs --work-dir /home/ubuntu/data/letsencrypt/work --config-dir /home/ubuntu/data/letsencrypt/config certonly --dns-route53 -d backend.deervision.studio
else
  echo "$(date '+%Y-%m-%d %H:%M:%S') - Certificate already exist" >> /home/ubuntu/userdata.log
fi

#Schedule renew certificate (Let's encrypt)
echo "$(date '+%Y-%m-%d %H:%M:%S') - Scheduling renew certificate" >> /home/ubuntu/userdata.log
echo -e "#"'!'"/bin/bash\n\ncertbot --logs-dir /home/ubuntu/data/letsencrypt/logs --work-dir /home/ubuntu/data/letsencrypt/work --config-dir /home/ubuntu/data/letsencrypt/config renew --dns-route53\nsudo service nginx reload" | sudo tee /etc/cron.daily/deervision-renew-cert
sudo chmod +x /etc/cron.daily/deervision-renew-cert

#Setup Nginx
echo "$(date '+%Y-%m-%d %H:%M:%S') - Installing nginx" >> /home/ubuntu/userdata.log
echo -e "limit_req_zone \$binary_remote_addr zone=req_zone:500m rate=${maxRequestsBySecond}r/s;\n" \
  "server {\n" \
  "  listen 443;\n" \
  "  listen [::]:443;\n" \
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
  "    proxy_set_header X-Forwarded-For \$remote_addr;\n" \
  "    proxy_set_header X-Real-IP \$remote_addr;\n" \
  "    proxy_pass http://127.0.0.1:8080;\n" \
  "  }\n" \
  "}" | sudo tee /etc/nginx/sites-available/reverseproxy
sudo rm /etc/nginx/sites-enabled/default
sudo ln -s /etc/nginx/sites-available/reverseproxy /etc/nginx/sites-enabled/reverseproxy
sudo systemctl restart nginx

#End script log file
echo "$(date '+%Y-%m-%d %H:%M:%S') - End script execution" >> /home/ubuntu/userdata.log
echo "" >> /home/ubuntu/userdata.log
