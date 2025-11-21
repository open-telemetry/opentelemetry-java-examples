FROM eclipse-temurin:21.0.9_10-jre@sha256:4332b7939ba5b7fabde48f4da21ebe45a4f8943d5b3319720c321ac577e65fb1

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
