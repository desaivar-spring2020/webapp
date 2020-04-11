echo "***************************************************"
echo "APPLICATION START BEGINS"
echo "***************************************************"


## checkpoint commdand
cd /home/ubuntu
mkdir applicationStart


## move to /home/ubuntu
cd /home/ubuntu
pwd
ls -ltrh


## open tar
tar -xzvf webapp.tar.gz


## generate build
cd /home/ubuntu/project
mvn clean install > project_build_creation.out


## bring build file to home location
cp /home/ubuntu/project/target/ROOT*.jar /home/ubuntu/.


## kill tomcat (redundant command, just to be safe)
sudo kill -9 $(pgrep java)
sudo pkill -9 -f tomcat
sleep 10


## configure and start aws cloudwatch service
sudo cp /home/ubuntu/project/cloudwatch-config.json /opt/aws/amazon-cloudwatch-agent/.
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
    -a fetch-config \
    -m ec2 \
    -c file:/opt/aws/amazon-cloudwatch-agent/cloudwatch-config.json \
    -s
sudo systemctl start amazon-cloudwatch-agent.service


## run jar file
nohup java -jar /home/ubuntu/ROOT*.jar server.port=8080 > /home/ubuntu/application-execution.out 2>&1 &


## make sure webapp is running ####
ps -eaf | grep "java"





echo "***************************************************"
echo "APPLICATION START ENDS"
echo "***************************************************"
