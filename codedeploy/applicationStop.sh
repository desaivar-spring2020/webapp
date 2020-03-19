#
cd /home/ubuntu
sudo kill -9 $(lsof -t -i:8080) nohup java -jar ROOT*.jar server.port=8080 & > /home/ubuntu/application-execution.out &