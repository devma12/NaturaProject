# official image maven:3.6-jdk-14-alpine unknown yet
FROM maven:3.6-jdk-14 AS builder

WORKDIR /usr/src/server-app

COPY pom.xml .

RUN mvn -e -B dependency:resolve

COPY src ./src

RUN mvn -e -B package -DskipTests

# use jre image for next stage if jre official image exist
FROM openjdk:14-jdk-alpine

WORKDIR /usr/src/server-app

ARG JAR_FILE=/usr/src/server-app/target/*.jar

COPY --from=builder ${JAR_FILE} backend-app-server.jar

ENTRYPOINT ["java","-jar","backend-app-server.jar"]