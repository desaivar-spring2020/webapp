echo "***************************************************"
echo "AFTER INSTALL BEGINS"
echo "***************************************************"

# move to /home/ubuntu
cd /home/ubuntu
pwd
ls -ltrh

# open tar
tar -xzvf webapp.tar.gz

# generate build
cd /home/ubuntu/project
mvn clean install > project_build_creation.out

# copy build file to /home/ubuntu
cp /home/ubuntu/project/target/ROOT*.jar /home/ubuntu

# make next file executable
sudo chmod 777 /home/ubuntu/codedeploy/applicationStart.sh

echo "***************************************************"
echo "AFTER INSTALL ENDS"
echo "***************************************************"