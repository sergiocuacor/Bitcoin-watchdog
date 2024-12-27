#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR/.."

# Check if .env exists and source it only if it does
if [ -f ".env" ]; then
    source .env
fi

# Check if environment variables are set (either from .env or GitHub Actions)
if [ -z "$EMAIL_USERNAME" ] || [ -z "$EMAIL_PASSWORD" ]; then
    echo "Error: Required environment variables are not set"
    exit 1
fi

# Find the correct JAR file (excluding sources and javadoc JARs)
JAR_FILE=$(find target/ -name "*.jar" ! -name "*sources.jar" ! -name "*javadoc.jar" | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "Error: No JAR file found in target directory. Did you run 'mvn package'?"
    exit 1
fi

echo "Using JAR file: $JAR_FILE"
java -jar "$JAR_FILE"