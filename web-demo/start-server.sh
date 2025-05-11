#!/bin/bash

# Start a simple HTTP server to serve the web demo
echo "Starting Flappy Bird web demo server on port 8080..."
echo "To view the game, open the following URL in your browser:"
echo "http://localhost:8080/flappy-bird-web.html"
echo ""
echo "If you're running in GitHub Codespaces, click on the Ports tab,"
echo "find port 8080, and click on the globe icon to open it in your browser."
echo ""
echo "Press Ctrl+C to stop the server."

cd /workspaces/flappy-birds/web-demo
python3 -m http.server 8080
