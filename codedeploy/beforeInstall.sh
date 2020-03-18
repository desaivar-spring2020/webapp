# close application and clear jar from the location
cd /home/ubuntu


# clear build_creation location
cd /home/ubuntu
pwd
rm webapp.tar.gz
rm -rf project
rm ROOT*.jar
rm appspec.yml
rm -rf codedeploy

# kill tomcat process
pkill -9 -f tomcat

cd /home/ubuntu
