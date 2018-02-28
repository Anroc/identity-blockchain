#!/bin/sh

# git clone git@gitlab.tubit.tu-berlin.de:thatmann/iosl1718-IdentityBlockchain.git

echo "##### Pulling #####"
cd ../source
./gradlew bootRepackage

echo ""
echo "##### Building Discovery Service #####"
# build DiscoveryService
cd discovery-service
sudo docker kill DiscoveryService
sudo docker rm DiscoveryService
sudo docker build -t blockchain-identity/discovery-service:latest .
sudo docker run -d -p 8101:8080 --name DiscoveryService blockchain-identity/discovery-service:latest
cd ..

echo ""
echo "##### Building Frontend #####"

# frotnend
cd client-frontend
sudo docker kill Frontend
sudo docker rm Frontend
sudo docker build -t blockchain-identity/frontend:latest .
sudo docker run -d -p 1111:3000 --name Frontend blockchain-identity/frontend:latest
cd ..

echo ""
echo "##### Waiting for Discovery Service #####"
until $(curl --output /dev/null --silent --head --fail http://localhost:8101/heartbeat); do
    echo '.'
    sleep 5
done
echo "Discovery Service is ready: Will proceed."

echo ""
echo "##### Building Core-Logic-Goverement #####"

# core-logic goverment
cd provider
sudo docker kill CoreLogic-gov
sudo docker rm CoreLogic-gov
sudo docker build -t blockchain-identity/core-logic-government:latest --build-arg ACTIVE_PROFILE=production-government .
sudo docker run -d -p 8100:8080 --name CoreLogic-gov blockchain-identity/core-logic-government:latest
cd .. core-logic

sleep 5
echo ""
echo "##### Building Core-Logic-Provider #####"

# core-logic provider
cd provider
sudo docker kill CoreLogic-prov
sudo docker rm CoreLogic-prov
sudo docker build -t blockchain-identity/core-logic-provider:latest --build-arg ACTIVE_PROFILE=production-provider .
sudo docker run -d -p 8102:8080 --name CoreLogic-prov -v provider-data:/root/.ethereum/blockchain-identity blockchain-identity/core-logic-provider:latest
cd .. core-logic

sleep 5
echo ""
echo "##### Building Core-Logic-User #####"


sleep 5
# core-logic user
cd user
sudo docker kill CoreLogic-User
sudo docker rm CoreLogic-User
sudo docker build -t blockchain-identity/core-logic-user:latest .
sudo docker run -d -p 1112:8080 --name CoreLogic-User -v user-data:/root/.ethereum/blockchain-identity blockchain-identity/core-logic-user:latest
cd .. core-logic

echo ""
echo "##### Clean up ######"

# clean up
sudo docker image prune -f
