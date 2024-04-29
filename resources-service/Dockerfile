FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/resources-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

docker run -d -p 27030:27017 --name my-auth-container -v /Users/alipeker/Desktop/db/auth:/data/db mongo