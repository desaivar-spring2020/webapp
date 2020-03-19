# move to project location and start application
# untar the zip.
echo "***************************************************"
echo "APPLICATION START BEGINS"
echo "***************************************************"

cd /home/ubuntu
tar -xzvf webapp.tar.gz

# move to project root location
cd /home/ubuntu/project

# generate build
mvn clean install

# copy build file to location from which it should run..
cp /home/ubuntu/project/target/ROOT*.jar /home/ubuntu

cd /home/ubuntu
sudo pkill -9 -f tomcat
sleep 10

nohup java -jar /home/ubuntu/ROOT*.jar server.port=8080 > /home/ubuntu/application-execution.out 2>&1 &

echo "***************************************************"
echo "APPLICATION START ENDS"
echo "***************************************************"
