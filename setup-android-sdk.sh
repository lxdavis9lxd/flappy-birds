#!/bin/bash

# Create a directory for the Android SDK
mkdir -p $HOME/android-sdk

# Download the command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O cmdline-tools.zip
unzip -q cmdline-tools.zip -d $HOME/android-sdk/
mv $HOME/android-sdk/cmdline-tools $HOME/android-sdk/latest
mkdir -p $HOME/android-sdk/cmdline-tools
mv $HOME/android-sdk/latest $HOME/android-sdk/cmdline-tools/

# Set environment variables
export ANDROID_HOME=$HOME/android-sdk
export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH

# Accept licenses
yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses

# Install necessary packages
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "platforms;android-33" "build-tools;33.0.2" "platform-tools"

# Print SDK location for verification
echo "Android SDK installed at: $ANDROID_HOME"
ls -la $ANDROID_HOME
