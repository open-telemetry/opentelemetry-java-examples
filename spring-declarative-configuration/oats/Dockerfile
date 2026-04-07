FROM eclipse-temurin:25.0.2_10-jre@sha256:541cd02a3920c1f10b2f34af7b7144414bc48f0b5dbc933a1def87699e5e5727

WORKDIR /usr/src/app/

ADD ./build/libs/spring-declarative-configuration.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "./app.jar" ]
