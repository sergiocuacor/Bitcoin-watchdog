#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )" # Find the script location and go to the project folder	
cd "$DIR/.." # this is where the .env file will be located if you want to test the program locally

# Check if .env file exists. If it does, load the config
if [ -f ".env" ]; then
    source .env
fi

# Check if environment variables are set (either from .env or GitHub Actions)
if [ -z "$EMAIL_USERNAME" ] || [ -z "$EMAIL_PASSWORD" ]; then # If username or password values are empty, throw an error and exit
    echo "Error: Environment variables are not set"
    exit 1
fi

# Find the JAR file within the target directory (but exclude javadoc and sources .JAR!) (takes only the first file found (head -1))
JAR_FILE=$(find target/ -name "*.jar" ! -name "*sources.jar" ! -name "*javadoc.jar" | head -1)

if [ -z "$JAR_FILE" ]; then # If there is not JAR file, throw an error and exit the program
    echo "Error: No JAR file found in target directory. Did you run 'mvn package'?"
    exit 1
fi

echo "Using JAR file: $JAR_FILE" # Show the JAR file that's going to be used and execute it 
java -jar "$JAR_FILE"