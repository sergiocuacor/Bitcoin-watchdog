#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Go to root directory
cd "$DIR/.."

# and then load env variables
source .env
java -jar bitcoin-watchdog.jar