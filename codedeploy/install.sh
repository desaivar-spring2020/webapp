
# copy the new bundle to desired location
#cd `ls -td -- /opt/codedeploy-agent/deployment-root/4801f4db-c413-4cbf-a0ac-fb4276d3588e/* | head -n 1`
#cp bundle.tar /home/ubuntu


echo "***************************************************"
echo "INSTALL BEGINS"
echo "***************************************************"

# make next file executable
sudo chmod 777 /home/ubuntu/codedeploy/afterInstall.sh


echo "***************************************************"
echo "INSTALL ENDS."
echo "***************************************************"