FROM openjdk:14-jdk-alpine

WORKDIR /usr/src/server-app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-app-server.jar

ENTRYPOINT ["java","-jar","backend-app-server.jar"]