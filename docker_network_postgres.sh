#!/bin/sh
docker network create bitewaysnetwork

docker run --name bitewaysdb --network  bitewaysnetwork -e POSTGRES_DB=biteways -e POSTGRES_USER=bitewaysuser -e POSTGRES_PASSWORD=12345 -p 5442:5432 -d postgres:alpine

docker build -t biteways_img .

docker run -p8080:8080 --network bitewaysnetwork -e DB_URL=bitewaysdb -e DB_PORT=5442 -it biteways_img


