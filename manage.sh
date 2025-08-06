#!/bin/bash

# Pure Healthy Eats Application Manager

case "$1" in
  start)
    echo "Starting Pure Healthy Eats..."
    # Start Spring Boot app with proper backgrounding
    nohup ./mvnw spring-boot:run > app.log 2>&1 < /dev/null &
    APP_PID=$!
    echo $APP_PID > app.pid
    disown $APP_PID
    
    echo "Waiting for Spring Boot to start (15 seconds)..."
    sleep 15
    
    # Start tunnel with proper backgrounding
    nohup lt --port 8080 > tunnel.log 2>&1 < /dev/null &
    TUNNEL_PID=$!
    echo $TUNNEL_PID > tunnel.pid
    disown $TUNNEL_PID
    
    echo "Waiting for tunnel to initialize (5 seconds)..."
    sleep 5
    echo "Application started!"
    echo "URL: $(cat tunnel.log | grep 'your url is' | tail -1)"
    echo "Password: $(curl -s https://loca.lt/mytunnelpassword)"
    echo ""
    echo "✅ Processes are now running detached from terminal"
    echo "✅ You can safely close this terminal/Warp"
    ;;
  
  stop)
    echo "Stopping Pure Healthy Eats..."
    if [ -f app.pid ]; then
      kill $(cat app.pid) 2>/dev/null
      rm app.pid
    fi
    if [ -f tunnel.pid ]; then
      kill $(cat tunnel.pid) 2>/dev/null
      rm tunnel.pid
    fi
    pkill -f "spring-boot:run" 2>/dev/null
    pkill -f "lt --port" 2>/dev/null
    echo "Application stopped!"
    ;;
  
  status)
    echo "Checking application status..."
    if pgrep -f "spring-boot:run" > /dev/null; then
      echo "✅ Spring Boot app: RUNNING"
    else
      echo "❌ Spring Boot app: NOT RUNNING"
    fi
    
    if pgrep -f "lt --port" > /dev/null; then
      echo "✅ Tunnel: RUNNING"
      if [ -f tunnel.log ]; then
        echo "🌐 URL: $(cat tunnel.log | grep 'your url is' | tail -1)"
        echo "🔑 Password: $(curl -s https://loca.lt/mytunnelpassword)"
      fi
    else
      echo "❌ Tunnel: NOT RUNNING"
    fi
    ;;
  
  logs)
    echo "=== Application Logs ==="
    if [ -f app.log ]; then
      tail -f app.log
    else
      echo "No application logs found"
    fi
    ;;
    
  url)
    if [ -f tunnel.log ]; then
      echo "🌐 URL: $(cat tunnel.log | grep 'your url is' | tail -1)"
      echo "🔑 Password: $(curl -s https://loca.lt/mytunnelpassword)"
    else
      echo "No tunnel running"
    fi
    ;;
  
  *)
    echo "Usage: $0 {start|stop|status|logs|url}"
    echo ""
    echo "Commands:"
    echo "  start   - Start the application and tunnel"
    echo "  stop    - Stop the application and tunnel"
    echo "  status  - Check if application is running"
    echo "  logs    - Show application logs"
    echo "  url     - Show current public URL and password"
    exit 1
    ;;
esac
