#!/usr/bin/env bash

PORT=${1}

echo "Start server with ${PORT}"
chmod +x ./gradlew
./gradlew bootRun -PjvmArgs="-Dserver.port=${PORT}"
