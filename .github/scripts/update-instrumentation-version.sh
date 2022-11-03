#!/bin/bash -e

version=$1

sed -Ei "s/otelInstrumentationVersion = \"[^\"]*\"/otelInstrumentationVersion = \"$version\"/" build.gradle
