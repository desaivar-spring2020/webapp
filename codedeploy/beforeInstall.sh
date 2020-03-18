# close application and clear jar from the location
cd /home/ubuntu
mkdir reached-beforeinstall

# clear build_creation location
cd /home/ubuntu
pwd
rm bundle.tar
rm -rf project
rm ROOT*.jar

# kill tomcat process
pkill -9 -f tomcat

cd /home/ubuntu
mkdir completed-beforeinstall
