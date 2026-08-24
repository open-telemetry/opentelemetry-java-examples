FROM eclipse-temurin:25.0.4_7-jre@sha256:f9e65324a37f28209ce7dd0e5149a7aa954520ed936fb87813cf6ded2400a112

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
