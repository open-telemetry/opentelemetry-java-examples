# Resource Detection Example - GCP

An example application that shows what resource attributes will be detected by the [GCP Resource Detector](https://github.com/open-telemetry/opentelemetry-java-contrib/tree/main/gcp-resources) and how the [Autoconfigure Resource Provider SPI](https://opentelemetry.io/docs/languages/java/configuration/#spi-service-provider-interface) automatically *attaches* the detected resource attributes to the generated telemetry.

*For users looking to use the GCP Resource Detector with OpenTelemetry Java Agent for automatic instrumentation, refer to the [instructions for enabling resource providers that are disabled by default](https://opentelemetry.io/docs/languages/java/automatic/configuration/#enable-resource-providers-that-are-disabled-by-default).*

## Prerequisites

### Get Google Cloud Credentials on your machine

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

#### Export the Google Cloud Project ID to `GOOGLE_CLOUD_PROJECT` environment variable

```shell
export GOOGLE_CLOUD_PROJECT="my-awesome-gcp-project-id"
```

### Setup Google Cloud Artifact Registry

> [!NOTE]
> This step is only required if you want to run the application in GKE

```shell
export ARTIFACT_REGISTRY="my-container-registry"
export REGISTRY_LOCATION="us-central1"
gcloud artifacts repositories create ${ARTIFACT_REGISTRY} --repository-format=docker --location=${REGISTRY_LOCATION} --description="Resource detection sample on GKE"
```

## Running in Google Kubernetes Engine

> [!NOTE]
> Make sure that kubectl is installed and configured to access a GKE cluster. Relevant documentation can be found in the [GKE cluster access guide](https://cloud.google.com/kubernetes-engine/docs/how-to/cluster-access-for-kubectl#run_against_a_specific_cluster).

To spin it up on your own GKE cluster, run the following:

```shell
./gradlew :opentelemetry-examples-resource-detection-gcp:jib --image="$REGISTRY_LOCATION-docker.pkg.dev/$GOOGLE_CLOUD_PROJECT/$ARTIFACT_REGISTRY/hello-resource-java"

sed -e s/%GOOGLE_CLOUD_PROJECT%/$GOOGLE_CLOUD_PROJECT/g \
    -e s/%REGISTRY_LOCATION%/$REGISTRY_LOCATION/g \
    -e s/%ARTIFACT_REGISTRY%/$ARTIFACT_REGISTRY/g \
 resource-detection-gcp/k8s/job.yaml | kubectl apply -f -
```

This will run the application as a GKE workload. You can view it from the `Workloads` tab under the `Resource Management` section on GKE console.

The generated logs can be viewed under the `Logs` tab on the `Job Details` page. These logs will show the detected resource attributes for GKE.
The detected attributes will contain the attributes detected by the GCP resource detector as well as the attributes attached by the autoconfigure module.

## Running the application locally

> [!NOTE]
> Resource attributes won't be detected in unsupported environments. You can find a [list of supported environments in the GCP detector documentation](https://github.com/open-telemetry/opentelemetry-java-contrib/tree/main/gcp-resources).

You can run the application locally as well:

From the root of the repository,

```shell
cd resource-detection-gcp && gradle run
```

The detected resource attributes would depend on the environment in which the application is run.

## Viewing the result

The example produces a single span and uses [logging OTLP JSON exporter](https://opentelemetry.io/docs/languages/java/configuration/#properties-exporters) to export the produced span.
After running the example successfully, you should see the emitted span in JSON form via JUL. It should look something like:

```
INFO: {"resource":{"attributes": ... }}
```

## Cleanup

Cleanup any Google Cloud Resources created to run this sample.

```shell
# Delete the Job
kubectl delete job hello-resource-java

# Delete Artifact Registry
gcloud artifacts repositories delete $ARTIFACT_REGISTRY --location=$REGISTRY_LOCATION
```
