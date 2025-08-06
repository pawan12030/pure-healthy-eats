#!/bin/bash

# Pure Healthy Eats Background Launcher
cd "$(dirname "$0")"

echo "Starting Pure Healthy Eats in background..."

# Function to start Spring Boot
start_springboot() {
    echo "Starting Spring Boot application..."
    nohup ./mvnw spring-boot:run > app.log 2>&1 < /dev/null &
    APP_PID=$!
    echo $APP_PID > app.pid
    echo "Spring Boot started with PID: $APP_PID"
    return 0
}

# Function to start tunnel
start_tunnel() {
    echo "Starting tunnel..."
    nohup lt --port 8080 > tunnel.log 2>&1 < /dev/null &
    TUNNEL_PID=$!
    echo $TUNNEL_PID > tunnel.pid
    echo "Tunnel started with PID: $TUNNEL_PID"
    return 0
}

# Start Spring Boot
start_springboot

# Wait for Spring Boot to be ready
echo "Waiting for Spring Boot to start (20 seconds)..."
sleep 20

# Check if Spring Boot is running
if ! pgrep -f "spring-boot:run" > /dev/null; then
    echo "❌ Spring Boot failed to start. Check app.log for errors."
    exit 1
fi

# Start tunnel
start_tunnel

# Wait for tunnel to be ready
echo "Waiting for tunnel to initialize (10 seconds)..."
sleep 10

# Show results
echo ""
echo "🎉 Application started successfully!"
echo ""
if [ -f tunnel.log ]; then
    URL=$(cat tunnel.log | grep 'your url is' | tail -1)
    if [ ! -z "$URL" ]; then
        echo "🌐 $URL"
        echo "🔑 Password: $(curl -s https://loca.lt/mytunnelpassword)"
    else
        echo "⚠️  Tunnel may still be initializing. Check tunnel.log"
    fi
fi

echo ""
echo "✅ Both processes are running detached from terminal"
echo "✅ You can safely close this terminal/Warp app"
echo ""
echo "To check status later: ./manage.sh status"
echo "To stop services: ./manage.sh stop"
