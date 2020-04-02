echo "***************************************************"
echo "BEFORE INSTALL BEGINS"
echo "***************************************************"
# move to home location
cd /home/ubuntu
pwd
ls -ltrh


# clear home location
cd /home/ubuntu
pwd
sudo rm webapp.tar.gz
sudo rm -rf project
sudo rm ROOT*.jar
sudo rm appspec.yml
sudo rm -rf codedeploy

# kill tomcat process (redundant command, just to be safe)
sudo pkill -9 -f tomcat

# make next file executable
sudo chmod 777 /home/ubuntu/codedeploy/install.sh


echo "***************************************************"
echo "BEFORE INSTALL ENDS"
echo "***************************************************"
