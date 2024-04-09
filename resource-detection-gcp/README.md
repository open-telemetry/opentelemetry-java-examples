# Resource Detection Example - GCP

An example application that shows what resource attributes will be detected by the [GCP Resource Detector](https://github.com/open-telemetry/opentelemetry-java-contrib/tree/main/gcp-resources) and how the [Autoconfigure Resource Provider SPI](https://github.com/open-telemetry/opentelemetry-java/tree/main/sdk-extensions/autoconfigure#resource-provider-spi) automatically *attaches* the detected resource attributes to the generated telemetry.

*For users looking to use the GCP Resource Detector with OpenTelemetry Java Agent for automatic instrumentation, refer to the instructions [here](https://opentelemetry.io/docs/languages/java/automatic/configuration/#enable-resource-providers-that-are-disabled-by-default).*

### Prerequisites

##### Get Google Cloud Credentials on your machine

```shell
gcloud auth application-default login
```
Executing this command will save your application credentials to default path which will depend on the type of machine -
- Linux, macOS: `$HOME/.config/gcloud/application_default_credentials.json`
- Windows: `%APPDATA%\gcloud\application_default_credentials.json`

**NOTE: This method of authentication is not recommended for production environments.**

Next, export the credentials to `GOOGLE_APPLICATION_CREDENTIALS` environment variable -

For Linux & MacOS:
```shell
export GOOGLE_APPLICATION_CREDENTIALS=$HOME/.config/gcloud/application_default_credentials.json
```

For Windows:
```shell
SET GOOGLE_APPLICATION_CREDENTIALS=%APPDATA%\gcloud\application_default_credentials.json
```

##### Export the Google Cloud Project ID to `GOOGLE_CLOUD_PROJECT` environment variable:

```shell
export GOOGLE_CLOUD_PROJECT="my-awesome-gcp-project-id"
```

## Running in Google Kubernetes Engine

To spin it up on your own GKE cluster, run the following:
```shell
./gradlew :opentelemetry-examples-resource-detection-gcp:jib --image="gcr.io/$GOOGLE_CLOUD_PROJECT/hello-resource-java"

sed s/%GOOGLE_CLOUD_PROJECT%/$GOOGLE_CLOUD_PROJECT/g \
 resource-detection-gcp/k8s/job.yaml | kubectl apply -f -
```

This will run the application as a GKE workload. You can view it from the `Workloads` tab under the `Resource Management` section on GKE console.

The generated logs can be viewed under the `Logs` tab on the `Job Details` page. These logs will show the detected resource attributes for GKE.

## Running the application locally

> [!NOTE]
> Resource attributes won't be detected in unsupported environments. You can find a list of environments supported by the GCP detector [here](https://github.com/open-telemetry/opentelemetry-java-contrib/tree/main/gcp-resources).

You can run the application locally as well:

From the root of the repository,
```shell
cd resource-detection-gcp && gradle run
```

The detected resource attributes would depend on the environment in which the application is run.

## Viewing the result

The example produces a single span and uses [logging OTLP JSON exporter](https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md#logging-otlp-json-exporter) to export the produced span.
After running the example successfully, you should see the emitted span in JSON form via JUL. It should look something like:

```
INFO: {"resource":{"attributes": ... }}
```
