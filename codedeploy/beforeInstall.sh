echo "***************************************************"
echo "BEFORE INSTALL BEGINS"
echo "***************************************************"
# close application and clear jar from the location
cd /home/ubuntu


# clear build_creation location
cd /home/ubuntu
pwd
sudo rm webapp.tar.gz
sudo rm -rf project
sudo rm ROOT*.jar
sudo rm appspec.yml
sudo rm -rf codedeploy

# kill tomcat process
sudo pkill -9 -f tomcat

cd /home/ubuntu

echo "***************************************************"
echo "BEFORE INSTALL ENDS"
echo "***************************************************"
