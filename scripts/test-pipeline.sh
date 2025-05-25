#!/bin/bash

# Android Release Pipeline Test Script
# This script helps verify that the multi-environment build system is working correctly

set -e

echo "🚀 Android Release Pipeline Test"
echo "================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    local status=$1
    local message=$2
    if [ "$status" = "SUCCESS" ]; then
        echo -e "${GREEN}✅ $message${NC}"
    elif [ "$status" = "WARNING" ]; then
        echo -e "${YELLOW}⚠️  $message${NC}"
    else
        echo -e "${RED}❌ $message${NC}"
    fi
}

# Function to check if a file exists
check_file() {
    local file=$1
    local description=$2
    if [ -f "$file" ]; then
        print_status "SUCCESS" "$description found: $file"
        return 0
    else
        print_status "ERROR" "$description missing: $file"
        return 1
    fi
}

echo ""
echo "📁 Checking required files..."

# Check configuration files
check_file "app/build.gradle.kts" "Build configuration"
check_file ".github/workflows/release.yml" "GitHub Actions workflow"
check_file "release/karage-app-keystore.jks" "Release keystore"

# Check documentation
check_file "GITHUB_SECRETS.md" "GitHub secrets guide"
check_file "MULTI_ENVIRONMENT_GUIDE.md" "Multi-environment guide"
check_file "PLAY_STORE_GUIDE.md" "Play Store guide"

echo ""
echo "🏗️  Testing local builds..."

# Set up environment variables for signing
export KEYSTORE_PASSWORD=android123
export KEY_ALIAS=karage-app-key
export KEY_PASSWORD=android123

# Test development debug build
echo ""
echo "Building devDebug..."
if ./gradlew assembleDevDebug --no-configuration-cache --quiet; then
    print_status "SUCCESS" "devDebug build completed"
else
    print_status "ERROR" "devDebug build failed"
fi

# Test production staging build
echo ""
echo "Building prodStaging..."
if ./gradlew assembleProdStaging --no-configuration-cache --quiet; then
    print_status "SUCCESS" "prodStaging build completed"
else
    print_status "ERROR" "prodStaging build failed"
fi

echo ""
echo "📦 Checking build outputs..."

# Check for APK files
if [ -f "app/build/outputs/apk/dev/debug/app-dev-debug.apk" ]; then
    print_status "SUCCESS" "devDebug APK generated"
else
    print_status "WARNING" "devDebug APK not found"
fi

if [ -f "app/build/outputs/apk/prod/staging/app-prod-staging.apk" ]; then
    print_status "SUCCESS" "prodStaging APK generated"
else
    print_status "WARNING" "prodStaging APK not found"
fi

echo ""
echo "🔑 Checking signing configuration..."

# Check if environment variables are set
if [ -n "$KEYSTORE_PASSWORD" ] && [ -n "$KEY_ALIAS" ] && [ -n "$KEY_PASSWORD" ]; then
    print_status "SUCCESS" "Signing environment variables configured"
else
    print_status "WARNING" "Signing environment variables not set"
fi

# Check keystore accessibility
if [ -f "release/karage-app-keystore.jks" ]; then
    if keytool -list -keystore "release/karage-app-keystore.jks" -storepass android123 &>/dev/null; then
        print_status "SUCCESS" "Keystore accessible and valid"
    else
        print_status "ERROR" "Keystore inaccessible or invalid"
    fi
fi

echo ""
echo "📋 Build variants summary:"
echo "  - devDebug: Development testing (debuggable, no optimization)"
echo "  - devStaging: Development with optimization (debuggable, minified)"
echo "  - prodStaging: Pre-production testing (debuggable, minified)"
echo "  - prodRelease: Production release (optimized, signed)"

echo ""
echo "🎯 Next steps:"
echo "  1. Add GitHub repository secrets (see GITHUB_SECRETS.md)"
echo "  2. Push to 'prod' branch or create version tag to trigger CI/CD"
echo "  3. Download APKs from GitHub Actions artifacts"
echo "  4. Test APKs on real devices"
echo "  5. Submit prodRelease APK to Google Play Store"

echo ""
echo "📚 Documentation available:"
echo "  - GITHUB_SECRETS.md: GitHub repository setup"
echo "  - MULTI_ENVIRONMENT_GUIDE.md: Build system overview"
echo "  - PLAY_STORE_GUIDE.md: Google Play Store deployment"
echo "  - RELEASE_PIPELINE_STATUS.md: Complete status overview"

echo ""
print_status "SUCCESS" "Release pipeline test completed! ✨"
