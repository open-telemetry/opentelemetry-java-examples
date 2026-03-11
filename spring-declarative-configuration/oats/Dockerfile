FROM eclipse-temurin:25.0.2_10-jre@sha256:91e59267939a3d14198d537b504af2d836c44894dde19919b2f22192c26841ef

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
