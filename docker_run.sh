#!/bin/sh
docker run -p 8080:8080 -e DB_URL=host.docker.internal --rm --name biteways_ctr -d biteways_img