#

echo "***************************************************"
echo "APPLICATION STOP BEGINS"
echo "***************************************************"

# move to home location
cd /home/ubuntu
mkdir applicationStop

# kill webapp processes, 1) java command that is running the application 2) tomcat server
sudo kill -9 $(lsof -t -i:8080) nohup java -jar /home/ubuntu/ROOT*.jar server.port=8080 > /home/ubuntu/application-execution.out 2>&1 &
sudo pkill -9 -f tomcat

# make next file executable
sudo chmod 777 /home/ubuntu/codedeploy/beforeInstall.sh


echo "***************************************************"
echo "APPLICATION STOP ENDS"
echo "***************************************************"