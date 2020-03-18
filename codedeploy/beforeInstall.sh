# close application and clear jar from the location


# clear build_creation location
cd /home/ubuntu
pwd
rm bundle.tar
rm -rf project
rm ROOT*.jar

# kill tomcat process
pkill -9 -f tomcat
