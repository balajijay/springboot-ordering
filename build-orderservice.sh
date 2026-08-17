#!/bin/bash
echo "🚀 Building Microservice Ecosystem..."

# kill existing processes running already
kill -9 $(lsof -t -i:9092,8080,8081,8000,8083,8084,8085) 2>/dev/null

# Build API Gatewaye
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/api-gateway
./mvnw clean package -DskipTests

# Build Order Service
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/order-service
./mvnw clean package -DskipTests

# Build Inventory Service
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/inventory-service
./mvnw clean package -DskipTests

# Build Notification Service
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/notification-service
./mvnw clean package -DskipTests

# Build Address Service
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/address-service
./mvnw clean package -DskipTests

# Build Product Service
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/product-service
./mvnw clean package -DskipTests

echo "--------------------------------------------------------"
echo "🎉 All services have been built successfully !"
echo "--------------------------------------------------------"


