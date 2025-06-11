#!/bin/bash
set -e

echo "Cleaning and building eureka-server..."
cd eureka-server && ./gradlew clean build && cd ..

echo "Cleaning and building gym-crm..."
cd gym-crm && ./gradlew clean build && cd ..

echo "Cleaning and building trainer-workload-service..."
cd trainer-workload-service && ./gradlew clean build && cd ..
