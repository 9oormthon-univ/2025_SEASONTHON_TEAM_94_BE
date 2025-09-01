FROM openjdk:17-jdk-slim

VOLUME /app

EXPOSE 8080

COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
