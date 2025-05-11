# Web Demo for Flappy Bird Game

This is a browser-based version of the Flappy Bird game that can be run directly from GitHub Codespaces.

## How to Run

1. Open the HTML file in a browser:
   - From VS Code in Codespaces, right-click on `flappy-bird-web.html` and select "Open with Live Server" (if the Live Server extension is installed)
   - Or use the "Preview" feature in Codespaces to view the HTML file

2. Alternatively, you can serve the file using Python's built-in HTTP server:
   ```
   cd /workspaces/flappy-birds/web-demo
   python3 -m http.server 8080
   ```

## Game Controls

- Click or tap the screen to make the bird flap
- Press the space bar to make the bird flap
- Avoid the pipes and don't hit the ground
- Your score increases as you pass through pipes

## Features

- The web demo includes all the core gameplay mechanics of the Android version
- Animated bird character
- Randomly generated pipes
- Score tracking with high score persistence
- Game over and restart functionality
