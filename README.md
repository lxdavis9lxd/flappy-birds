# Flappy Bird Android Game

A simple Flappy Bird clone for Android, built with Kotlin.

## Web Demo Available!

For quick testing in GitHub Codespaces, check out the web-based version:

```bash
cd /workspaces/flappy-birds/web-demo
./start-server.sh
```

Then open port 8080 in your browser to play the game directly from your Codespace!

## Features

- Tap to make the bird flap and navigate through pipes
- Score tracking and high score persistence
- Game over and restart functionality
- Smooth animations and physics
- Custom vector graphics for all game elements
- Adaptive icons for modern Android devices

## Graphics

The game uses vector drawables for all graphical elements:
- Bird character with animation frames
- Scrolling background with clouds and hills
- Green pipes with highlights
- Scrolling ground with texture

## Project Structure

This project is organized as a standard Android application:

- `MainActivity.kt`: Entry point for the application
- `GameView.kt`: Main game surface where gameplay occurs
- `Bird.kt`: Manages the bird character, physics, and animations
- `Pipe.kt`: Handles pipe obstacles, collision detection, and movement
- `Background.kt`: Controls the game background
- `Ground.kt`: Manages the scrolling ground

## Running in GitHub Codespaces

While you can't directly run the Android app in Codespaces (as it requires an Android device or emulator), you can:

1. Try the web demo version available in the `web-demo` folder
2. Use the GitHub Codespace to make code changes
3. Export the project when ready to test on a real device

## Exporting the Project

To use this code in Android Studio:

1. Clone this repository to your local machine
2. Open the project in Android Studio
3. Let Gradle sync complete
4. Run on an emulator or physical device

## Getting Started

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- Android SDK 21 or higher
- Kotlin 1.8.0 or higher

### Opening the Project

1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to and select the project directory
4. Wait for Gradle sync to complete

### Running the Game

1. Connect an Android device or use the emulator
2. Press the run button in Android Studio
3. Select your deployment target and confirm

## How to Play

- Tap the screen to make the bird flap upward
- Navigate through the gaps between pipes
- Each pipe you pass awards one point
- Don't hit the pipes or the ground!

## Customization

This basic implementation can be extended with:

- Better graphics and animations
- Sound effects and music
- Different difficulty levels
- Power-ups and special abilities