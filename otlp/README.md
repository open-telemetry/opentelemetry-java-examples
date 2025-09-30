### Running the OTLP example

## Prerequisites

* Java 17 or higher is required to build this example (to run Gradle)
* Java 8 or higher is required to run the compiled example
* Docker compose

## Run

Assuming you're on a unix-alike (mac, linux, etc):

In a shell:
```shell
cd docker
docker-compose up
```

In a separate shell:
```shell
../gradlew run
```