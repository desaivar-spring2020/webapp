# copy the new bundle to desired location
cd `ls -td -- /opt/codedeploy-agent/deployment-root/4801f4db-c413-4cbf-a0ac-fb4276d3588e/* | head -n 1`
cp bundle.tar /home/ubuntu

# untar the zip
tar -xzvf bundle.tar

# move to project root location
cd project

# generate build
mvn clean install

# copy build file to location from which it should run
cp target/ROOT*.war /home/ubuntu
