FROM eclipse-temurin:25.0.2_10-jre@sha256:ad53b918fedaefd44135a226c5ecaa4828a3d72db2f6d520da96a62979a63ac6

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
