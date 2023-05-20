::docker login -u tczaplic

docker ps -a

docker start server

docker ps 

docker images

docker pull tczaplic/application:serverrest

docker run -d -p 8080:80 -p 44300:443 --name server tczaplic/application:serverrest

docker inspect server

curl -X GET "http://localhost:8080/File/Show" -H "accept: application/json"

::docker stop server

::docker rm server

pause
