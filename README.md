# Android App Development and Debugging with Real Devices

This repository contains an Android application that can be developed and debugged using a real Android device from within a dev container. This project serves as a starter kit for building Android applications with best practices for linting, testing, and code coverage.

## Prerequisites

- Docker installed on your host machine
- Visual Studio Code with the Dev Containers extension
- USB cable to connect your Android device
- Android device with USB debugging enabled
- JDK 17 or higher (pre-installed in the dev container)
- Gradle 8.0+ (pre-installed in the dev container)

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

## Project Setup

### Gradle Setup

This project uses the Gradle build system with Kotlin DSL. The project includes:

- Version catalog in `gradle/libs.versions.toml` for dependency management
- Kotlin DSL for build scripts
- Modularized project structure

To install additional Gradle plugins, add them to either:
- Root `build.gradle.kts` for project-wide plugins
- Module-level `build.gradle.kts` for module-specific plugins

Example of adding a new plugin to the root build file:

```kotlin
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
    id("com.android.test") version "8.1.0" apply false
    // Add new plugin here
    id("io.gitlab.arturbosch.detekt") version "1.23.1" apply false
}
```

Example of adding a new plugin to the app module:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Add new plugin here
    alias(libs.plugins.kotlin.kapt)
    id("jacoco")
}
```

## Building the App

### Basic Build Commands

```bash
# Assemble the debug build
./gradlew assembleDebug

# Assemble the release build
./gradlew assembleRelease

# Build all variants
./gradlew build
```

### Clean Build

```bash
./gradlew clean build
```

### Install Build on Connected Device

```bash
# Install debug build
./gradlew installDebug

# Install release build
./gradlew installRelease
```

## Testing Framework

This project includes a comprehensive testing strategy with:
- Unit tests with JUnit
- UI tests with Espresso
- Integration tests

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run all instrumented tests on connected device
./gradlew connectedAndroidTest

# Run specific test class (choose variant: devDebug, prodDebug, etc.)
./gradlew testDevDebugUnitTest --tests "karage.app.CalculatorTest"

# Run specific test method
./gradlew testDevDebugUnitTest --tests "karage.app.CalculatorTest.additionIsCorrect"
```

### Test Coverage

To generate test coverage reports:

```bash
# Generate test coverage report
./gradlew createDebugCoverageReport

# Generate Jacoco coverage report for unit tests 
./gradlew jacocoTestReport

# View the generated report
"$BROWSER" app/build/reports/coverage/debug/index.html
```

Coverage reports are generated in HTML format and can be found at:
- Unit tests: `app/build/reports/jacoco/jacocoTestReport/html/index.html`
- Instrumented tests: `app/build/reports/coverage/debug/index.html`

## Linting and Code Quality

### Running Lint Checks

```bash
# Run lint checks
./gradlew lint

# Generate lint report
./gradlew lintReport

# View the generated report
"$BROWSER" app/build/reports/lint-results.html
```

### Working with Lint Baselines

Lint baselines allow you to establish a record of existing lint issues while ensuring no new issues are introduced. This is useful for legacy projects or when implementing stricter lint rules gradually.

```bash
# Create a baseline file (captures current issues) - using dev debug variant
./gradlew lintDevDebug -Dlint.baselines.continue=true

# Run lint with existing baseline (only reports new issues)
./gradlew lintDevDebug lintProdDebug

# Show all issues including those in baseline
./gradlew lintDevDebug -Dlint.ignoreBaseline=true

# Update the baseline after fixing some issues
./gradlew updateLintBaseline
```

The baseline file (`app/lint-baseline.xml`) should be committed to source control to ensure consistent behavior across all development environments. When the baseline is present:

- Existing issues in the baseline won't fail the build
- New issues not in the baseline will fail the build
- Fixing issues in baseline doesn't automatically remove them from baseline
- Use `updateLintBaseline` to refresh the baseline after fixing issues

### Static Analysis

To run static analysis tools:

```bash
# Run Detekt (if configured)
./gradlew detekt

# Run Ktlint (if configured)
./gradlew ktlintCheck

# Format code with Ktlint
./gradlew ktlintFormat
```

## Continuous Integration

This project can be easily integrated with CI systems. Here's a basic workflow:

1. Build the app
2. Run unit tests
3. Run static analysis
4. Generate test coverage
5. Deploy to test devices or store

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

## Profiling and Performance

### Memory Profiling

```bash
# Dump memory info
adb shell dumpsys meminfo karage.app
```

### CPU Profiling

```bash
# Start method tracing
adb shell am start -n karage.app/karage.app.MainActivity --start-profiler /sdcard/profile.trace

# After some time, stop profiling and pull the file
adb pull /sdcard/profile.trace ./
```

## Best Practices Included in This Template

- **Architecture**: MVVM with clean architecture principles
- **UI**: Material Design components
- **Testing**: Unit tests, UI tests, and integration tests
- **Dependency Management**: Version catalog for consistent dependency versions
- **Code Quality**: Lint, static analysis, and code style enforcement
- **Build Automation**: Gradle build scripts for all common tasks
- **Documentation**: Comprehensive README and code documentation

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

### Gradle build errors

1. Check Gradle version compatibility:
```bash
./gradlew --version
```

2. Clean and rebuild:
```bash
./gradlew clean build --refresh-dependencies
```

## Dev Container Configuration

This project uses a dev container with the following key configurations:

- Android SDK pre-installed with platform tools
- USB device access through host mounting
- Java and Kotlin development environment
- All necessary Android development tools

The configuration can be viewed and modified in `.devcontainer/devcontainer.json`.
