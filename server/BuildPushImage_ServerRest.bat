::docker login -u tczaplic

docker images

::docker tag busprojectserverrest:latest tczaplic/application:serverrest

::docker rmi busprojectserverrest:latest

docker rmi tczaplic/application:serverrest

docker build -f BusProject.Server.Rest/Dockerfile.prod -t tczaplic/application:serverrest .

docker images

docker image ls --filter label=stage=serverrest_build

::docker image prune --filter label=stage=serverrest_build

docker push tczaplic/application:serverrest

::docker rmi tczaplic/application:serverrest

::docker images

::docker logout

pause
