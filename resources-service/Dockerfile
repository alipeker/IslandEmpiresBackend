FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/resources-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

docker run -d -p 27400:27017 --name building-mongo -v /Users/alipeker/Desktop/db/building2:/data/db mongo