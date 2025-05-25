# Multi-Environment Android Build Configuration

This project supports multiple build environments and types to facilitate different stages of development and deployment.

## 🏗️ Build Configuration Overview

### Environments (Product Flavors)
- **`dev`**: Development environment with debug-friendly settings
- **`prod`**: Production environment with optimized settings

### Build Types
- **`debug`**: Unoptimized build for debugging (no obfuscation, logging enabled)
- **`staging`**: Pre-production testing build (optimized but with logging)
- **`release`**: Production-ready build (fully optimized, no logging)

### Valid Build Combinations

| Environment | Debug | Staging | Release | Use Case |
|-------------|-------|---------|---------|----------|
| **dev**     | ✅     | ✅       | ❌       | Development, internal testing |
| **prod**    | ❌     | ✅       | ✅       | Pre-production, production |

## 📱 App Configurations

### Package Names
- **Dev Debug**: `karage.app.dev.debug`
- **Dev Staging**: `karage.app.dev.staging`
- **Prod Staging**: `karage.app.staging`
- **Prod Release**: `karage.app`

### App Names
- **Dev Debug**: "Karage Debug"
- **Dev Staging**: "Karage Staging"
- **Prod Staging**: "Karage Staging"
- **Prod Release**: "Karage"

### Build Features
| Feature | Debug | Staging | Release |
|---------|-------|---------|---------|
| Debugging | ✅ | ✅ | ❌ |
| Code Minification | ❌ | ✅ | ✅ |
| Resource Shrinking | ❌ | ✅ | ✅ |
| Logging | ✅ | ✅ | ❌ |
| App Signing | ❌ | ✅ | ✅ |

## 🔧 Building the App

### Using Build Scripts
```bash
# Development builds
./scripts/build-release.sh dev debug
./scripts/build-release.sh dev staging

# Production builds
./scripts/build-release.sh prod staging
./scripts/build-release.sh prod release
```

### Using Gradle Directly
```bash
# List all available build variants
./gradlew tasks --all | grep assemble

# Build specific variants
./gradlew assembleDevDebug
./gradlew assembleDevStaging
./gradlew assembleProdStaging
./gradlew assembleProdRelease
```

## 🔐 Signing Configuration

### Development Signing
- Debug builds use the default Android debug keystore
- No additional setup required

### Release Signing
- Staging and Release builds use the production keystore
- Keystore location: `release/karage-app-keystore.jks`
- Supports environment variable overrides for CI/CD

### Environment Variables for CI/CD
```bash
export KEYSTORE_PASSWORD="your_keystore_password"
export KEY_ALIAS="your_key_alias"
export KEY_PASSWORD="your_key_password"
```

## 🌐 API Configuration

Each build variant includes different API endpoints:

```kotlin
// Access in your code via BuildConfig
BuildConfig.API_BASE_URL
BuildConfig.ENABLE_LOGGING
BuildConfig.BUILD_TYPE
BuildConfig.ENVIRONMENT
```

### API Endpoints
- **Dev**: `https://api-dev.karage.app/`
- **Staging**: `https://api-staging.karage.app/`
- **Production**: `https://api.karage.app/`

## 📋 Build Outputs

APK files are generated in the following structure:
```
app/build/outputs/apk/
├── dev/
│   ├── debug/
│   │   └── app-dev-debug.apk
│   └── staging/
│       └── app-dev-staging.apk
└── prod/
    ├── staging/
    │   └── app-prod-staging.apk
    └── release/
        └── app-prod-release.apk
```

## 🚀 Deployment Workflows

### Development Workflow
1. **Local Development**: Use `dev debug` builds
2. **Feature Testing**: Use `dev staging` builds
3. **Integration Testing**: Use `prod staging` builds
4. **Production Release**: Use `prod release` builds

### CI/CD Triggers
- **Push to `staging` branch**: Builds `prod staging`
- **Push to `prod` branch**: Builds multiple variants
- **Git tags (`v*`)**: Builds production release
- **Manual dispatch**: Build any combination

## 🧪 Testing Strategy

### Unit Tests
Run for all build types:
```bash
./gradlew test
```

### Integration Tests
Run before staging/release builds:
```bash
./gradlew connectedAndroidTest
```

### Quality Checks
- **Lint**: `./gradlew lint`
- **Detekt**: `./gradlew detekt`
- **KtLint**: `./gradlew ktlintCheck`

## 📦 Installation Commands

```bash
# Install different variants side by side
adb install app/build/outputs/apk/dev/debug/app-dev-debug.apk
adb install app/build/outputs/apk/dev/staging/app-dev-staging.apk
adb install app/build/outputs/apk/prod/staging/app-prod-staging.apk
adb install app/build/outputs/apk/prod/release/app-prod-release.apk
```

## 🔍 Troubleshooting

### Build Variant Not Found
If you get an error about a build variant not existing:
```bash
# Clean and sync
./gradlew clean
./gradlew --refresh-dependencies

# List available variants
./gradlew tasks --all | grep assemble
```

### Signing Issues
For signing problems in CI/CD:
1. Verify GitHub secrets are set correctly
2. Check keystore file exists and is accessible
3. Ensure environment variables are properly set

### Multiple APK Installation
Different variants can be installed simultaneously because they have different package names. This allows testing multiple versions on the same device.

## 📚 Best Practices

1. **Use appropriate builds for each stage**:
   - Development: `dev debug`
   - QA Testing: `dev staging` or `prod staging`
   - Production: `prod release`

2. **Always test staging builds** before releasing to production

3. **Keep keystores secure** and backed up

4. **Use different API endpoints** for different environments

5. **Monitor APK sizes** to ensure optimal performance

6. **Run quality checks** before releasing any staging or production builds
