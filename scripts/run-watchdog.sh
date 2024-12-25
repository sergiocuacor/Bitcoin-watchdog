#!/bin/bash

# Get script directory and move to project root
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR/.."

# Load environment variables
source .env

# Execute the application
echo "Starting Bitcoin Watchdog..."
java -jar bitcoin-watchdog.jar
EXIT_CODE=$?

# Wait for completion
sleep 30

# Check execution status and return to sleep mode
if [ $EXIT_CODE -eq 0 ]; then
    echo "Execution completed successfully, returning to sleep mode..."
    pmset sleepnow
else
    echo "Error during execution, exit code: $EXIT_CODE"
    echo "Returning to sleep mode anyway..."
    pmset sleepnow
fi