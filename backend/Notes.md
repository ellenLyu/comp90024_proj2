# CouchDB
couchdb vs couchbase
            :8091



https://github.com/klaemo/docker-couchdb/blob/29ed69965ed616a9d0df9a6ffa081773d86c78bc/1.6.1/Dockerfile

# Spring
FROM openjdk:11
EXPOSE 8080
ADD proj2-0.0.1-SNAPSHOT.jar proj.jar
ENTRYPOINT ["java", "-jar", "proj.jar"]


# Docker
Dockerfile on local -> build image -> use the image in the docker-compose

