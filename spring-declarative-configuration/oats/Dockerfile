FROM eclipse-temurin:25.0.2_10-jre@sha256:8f94f991adb7341cb5bcb39e5d7ee4ac944af94e54f5c67e9c8567ed064f0325

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
