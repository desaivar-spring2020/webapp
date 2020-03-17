# untar the zip
tar -xzvf webapp.tar.gz

# move to project root location
cd project

# generate build
mvn clean install

# copy build file to location from which it should run
cp target/ROOT*.war /home/ubuntu
