#!/bin/bash -e

version=$1

sed -Ei "s/otelSdkVersion = \"[^\"]*\"/otelSdkVersion = \"$version\"/" build.gradle
