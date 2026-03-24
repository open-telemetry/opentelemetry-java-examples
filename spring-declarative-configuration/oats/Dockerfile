FROM eclipse-temurin:25.0.2_10-jre@sha256:a9980cb3777d2b7b0d513800c3debc034c101530b96db4aadccb845f867fca9e

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
