# move to project location and start application
# untar the zip
cd /home/ubuntu
tar -xzvf webapp.tar.gz

# move to project root location
cd /home/ubuntu/project

# generate build
mvn clean install

# copy build file to location from which it should run.
cp /home/ubuntu/project/target/ROOT*.jar /home/ubuntu

cd /home/ubuntu
sudo pkill -9 -f tomcat
sleep 10
nohup java -jar ROOT*.jar & > /home/ubuntu/application-execution.out &

exit 1