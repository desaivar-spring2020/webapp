# move to project location and start application
# untar the zip.
echo "***************************************************"
echo "APPLICATION START BEGINS"
echo "***************************************************"

cd /home/ubuntu
pwd
ls -ltrh

mkdir applicationStart

sudo pkill -9 -f tomcat
sleep 10

nohup java -jar /home/ubuntu/ROOT*.jar server.port=8080 > /home/ubuntu/application-execution.out 2>&1 &

ps -eaf | grep "java"

sudo systemctl start amazon-cloudwatch-agent.service &

echo "***************************************************"
echo "APPLICATION START ENDS"
echo "***************************************************"
