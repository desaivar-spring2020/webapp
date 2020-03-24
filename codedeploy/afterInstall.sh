# clean /home/ubuntu location.

echo "***************************************************"
echo "AFTER INSTALL BEGINS"
echo "***************************************************"

pwd
ls -ltrh
cd /home/ubuntu

cd /home/ubuntu
tar -xzvf webapp.tar.gz

# move to project root location
cd /home/ubuntu/project

# generate build
mvn clean install

# copy build file to location from which it should run..
cp /home/ubuntu/project/target/ROOT*.jar /home/ubuntu

sudo chmod 777 /home/ubuntu/codedeploy/applicationStart.sh

echo "***************************************************"
echo "AFTER INSTALL ENDS"
echo "***************************************************"