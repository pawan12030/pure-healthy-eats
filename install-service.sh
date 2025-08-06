#!/bin/bash

# Install Pure Healthy Eats as a macOS service
CURRENT_DIR="$(cd "$(dirname "$0")" && pwd)"
SERVICE_NAME="com.purehealthyeats.app"
PLIST_FILE="$HOME/Library/LaunchAgents/$SERVICE_NAME.plist"

echo "Installing Pure Healthy Eats as a persistent macOS service..."

# Create the LaunchAgent plist file
cat > "$PLIST_FILE" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>Label</key>
    <string>$SERVICE_NAME</string>
    <key>ProgramArguments</key>
    <array>
        <string>$CURRENT_DIR/start-app-background.sh</string>
    </array>
    <key>WorkingDirectory</key>
    <string>$CURRENT_DIR</string>
    <key>RunAtLoad</key>
    <true/>
    <key>KeepAlive</key>
    <dict>
        <key>SuccessfulExit</key>
        <false/>
    </dict>
    <key>StandardOutPath</key>
    <string>$CURRENT_DIR/service.log</string>
    <key>StandardErrorPath</key>
    <string>$CURRENT_DIR/service-error.log</string>
</dict>
</plist>
EOF

echo "✅ Created LaunchAgent: $PLIST_FILE"

# Load the service
launchctl load "$PLIST_FILE"
if [ $? -eq 0 ]; then
    echo "✅ Service loaded successfully!"
    echo ""
    echo "🎉 Pure Healthy Eats is now installed as a persistent service!"
    echo ""
    echo "Benefits:"
    echo "• ✅ Runs automatically when you log in"
    echo "• ✅ Restarts automatically if it crashes"
    echo "• ✅ Survives computer reboots"
    echo "• ✅ Independent of terminal/Warp app"
    echo ""
    echo "Service management commands:"
    echo "  launchctl start $SERVICE_NAME     # Start service"
    echo "  launchctl stop $SERVICE_NAME      # Stop service"  
    echo "  launchctl unload \"$PLIST_FILE\"     # Uninstall service"
    echo ""
    echo "You can still use ./manage.sh status to check the application status"
else
    echo "❌ Failed to load service"
    exit 1
fi
