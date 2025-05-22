# Android App Development and Debugging with Real Devices

This repository contains an Android application that can be developed and debugged using a real Android device from within a dev container.

## Prerequisites

- Docker installed on your host machine
- Visual Studio Code with the Dev Containers extension
- USB cable to connect your Android device
- Android device with USB debugging enabled

## Setting Up Your Android Device

1. Enable Developer Options on your Android device:
   - Go to Settings > About phone
   - Tap on "Build number" 7 times until you see the message "You are now a developer!"

2. Enable USB Debugging:
   - Go to Settings > Developer options
   - Enable "USB debugging"

3. Connect your device to your computer with a USB cable.

## Starting the Dev Container

1. Open this project in Visual Studio Code
2. Click on the green button in the bottom-left corner or press F1 and select "Dev Containers: Reopen in Container"
3. The container will build and start with all necessary Android development tools

## Verifying Device Connection

Once your dev container is running and your device is connected, verify the connection:

```bash
adb devices
```

You should see your device listed like this:
```
List of devices attached
1060305023003940    device
```

If your device is shown as "unauthorized", check your phone's screen for a USB debugging authorization prompt and approve it.

## Installing APK Files on Your Device

### Install a debug APK:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Install a release APK (after signing):

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Launching the App

1. Get the package name and main activity (if not already known):

```bash
aapt dump badging app/build/outputs/apk/debug/app-debug.apk | grep "package\|launchable-activity"
```

2. Launch the app:

```bash
adb shell am start -n [package_name]/[main_activity]
```

Example:
```bash
adb shell am start -n karage.app/karage.app.MainActivity
```

## Monitoring App Logs

View logs in real-time:

```bash
adb logcat
```

Filter logs for your app only:

```bash
adb logcat | grep "$(adb shell ps | grep karage.app | awk '{print $2}')"
```

Or more specifically:

```bash
adb logcat *:E
```

## Useful ADB Commands

### App Management:

```bash
# List all installed packages
adb shell pm list packages

# Uninstall an app
adb uninstall karage.app

# Clear app data
adb shell pm clear karage.app
```

### Device Information:

```bash
# Get device properties
adb shell getprop

# Check device screen resolution
adb shell wm size

# Check device Android version
adb shell getprop ro.build.version.release
```

### File Transfer:

```bash
# Push a file to the device
adb push [local_file] [device_path]

# Pull a file from the device
adb pull [device_path] [local_path]
```

### Debugging:

```bash
# Take a screenshot
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png ./

# Record screen (Android 4.4+)
adb shell screenrecord /sdcard/video.mp4
# Press Ctrl+C to stop recording
adb pull /sdcard/video.mp4 ./
```

## Troubleshooting

### Device not detected

1. Reset ADB server:
```bash
adb kill-server
adb start-server
```

2. Check USB connection and cable
3. Ensure USB debugging is enabled
4. Try different USB ports

### Installation failures

1. Check for existing installations:
```bash
adb uninstall karage.app
```

2. Verify APK is properly built:
```bash
ls -la app/build/outputs/apk/debug/
```

### Permission issues

If the container can't access USB devices, make sure your dev container has the proper USB mounts and privileges as shown in the devcontainer.json file.

## Dev Container Configuration

This project uses a dev container with the following key configurations:

- Android SDK pre-installed with platform tools
- USB device access through host mounting
- Java and Kotlin development environment
- All necessary Android development tools

The configuration can be viewed and modified in `.devcontainer/devcontainer.json`.
