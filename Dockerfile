FROM openjdk:11.0-jdk-slim-buster

VOLUME /temp

COPY target/user-service-1.0.jar user-service.jar

ENTRYPOINT ["java","-jar","user-service.jar"]