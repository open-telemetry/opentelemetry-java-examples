#!/usr/bin/env bash
#MISE description="Lint markdown files"
#USAGE flag "--fix" help="Automatically fix issues"

set -e

if [ "${usage_fix:-false}" = "true" ]; then
  markdownlint-cli2 --fix "**/*.md"
else
  markdownlint-cli2 "**/*.md"
fi
