#!/bin/bash -e

version=$1

alpha_version=${version}-alpha

sed -Ei "s/otelVersion = \"[^\"]*\"/otelVersion = \"$version\"/" build.gradle
sed -Ei "s/otelAlphaVersion = \"[^\"]*\"/otelAlphaVersion = \"$alpha_version\"/" build.gradle
