FROM --platform=linux/arm64 openjdk:21-jdk-slim

VOLUME /app

EXPOSE 8080

COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
