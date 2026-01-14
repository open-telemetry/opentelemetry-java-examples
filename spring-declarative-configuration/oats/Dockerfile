FROM eclipse-temurin:25.0.1_8-jre@sha256:968f1917fcfa8250e72c39c68118798a5c86923a709c174578179b318f9033e6

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
