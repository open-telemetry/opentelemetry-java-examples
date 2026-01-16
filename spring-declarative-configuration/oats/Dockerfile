FROM eclipse-temurin:25.0.1_8-jre@sha256:96bccca442e8842a7366df338f5960e3342862ea6b8d42b97009a3dfd8602547

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
