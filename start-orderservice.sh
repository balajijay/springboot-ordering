#!/bin/bash
# Load configurations from your private local .env file if it exists
# Define the exact absolute path to your environment file
ENV_FILE_PATH="/Users/balajinarasimhan/Documents/workspace-aws-springboot/order-service.env"
if [ -f "$ENV_FILE_PATH" ]; then
    echo "🔑 Loading configuration variables..."
    export $(grep -v '^#' "$ENV_FILE_PATH" | xargs)
    
    echo "--------------------------------------------------"
    echo "🔎 Loaded provider: '$smtp_provider'"
    echo "🔎 Loaded username: '$smtp_email_username'"
    echo "🔎 Loaded password length: ${#smtp_email_password} characters"
    echo "--------------------------------------------------"
else
    echo "❌ ERROR: File not found at $ENV_FILE_PATH"
fi

# 💡 GLOBAL JVM OPTIONS FOR GENERATIONAL ZGC
# Defining this here lets us easily reuse it for all your Spring Boot services
ZGC_CORE="-XX:+UseZGC -XX:+ZGenerational"
# 🧠 TARGETED MEMORY PROFILES
# API Gateway needs less heap space than data/business logic microservices
OPTS_GATEWAY="$ZGC_CORE -Xmx256m"
OPTS_SERVICE="$ZGC_CORE -Xmx512m"

echo "🚀 Starting Microservice Ecosystem..."

# 1. Start Native Kafka Broker
echo "⚙️  Launching Native Kafka (KRaft mode)..."
cd /users/balajinarasimhan/projects/order-service/kafka_2.13-4.3.1
bin/kafka-server-start.sh config/server.properties > kafka.log 2>&1 &
KAFKA_PID=$!

# Wait for Kafka port 9092 to be ready
echo "⏳ Waiting for Kafka to bind to port 9092..."
while ! nc -z localhost 9092; do   
  sleep 1
done
echo "✅ Kafka is up and listening!"

# 2. Start Order Service
echo "📦 Launching Order Service on Port 8080..."
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/order-service
java $OPTS_SERVICE -jar target/order-service-0.0.1-SNAPSHOT.jar > order-service.log 2>&1 &
ORDER_PID=$!

# 3. Start Inventory Service
echo "🏪 Launching Inventory Service on Port 8081..."
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/inventory-service
java $OPTS_SERVICE -jar target/inventory-service-0.0.1-SNAPSHOT.jar > inventory-service.log 2>&1 &
INVENTORY_PID=$!

# 4. Start API Gateway Service
echo "🔒 Launching API Gateway on Port 8000..."
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/api-gateway
java $OPTS_GATEWAY -jar target/api-gateway-0.0.1-SNAPSHOT.jar > api-gateway.log 2>&1 &
GATEWAY_PID=$!

# 5. Start Notification Service
# Spring boot will automatically capture the 'smtp_provider', 'email_username', and 'email_password' exported from your .env file
echo "📩 Launching Notification Service on Port 8083..."
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/notification-service
java $OPTS_SERVICE -jar target/notification-service-0.0.1-SNAPSHOT.jar > notification-service.log 2>&1 &
NOTIFICATION_PID=$!

# 5. Start Address Service
echo "📩 Launching Address Service on Port 8084.."
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/address-service
java $OPTS_SERVICE -jar target/address-service-0.0.1-SNAPSHOT.jar > address-service.log 2>&1 &
ADDRESS_PID=$!

# 5. Start Product Service
echo "📩 Launching Product Service on Port 8085.."
cd /Users/balajinarasimhan/Documents/workspace-aws-springboot/product-service
java $OPTS_SERVICE -jar target/product-service-0.0.1-SNAPSHOT.jar > product-service.log 2>&1 &
PRODUCT_PID=$!

echo "--------------------------------------------------------"
echo "🎉 All services running safely in the background!"
echo "📄 Logs are streaming to kafka.log, order-service.log, and inventory-service.log"
echo "🛑 Run this command to stop everything:"
echo "kill $KAFKA_PID $ORDER_PID $INVENTORY_PID"
echo "--------------------------------------------------------"
