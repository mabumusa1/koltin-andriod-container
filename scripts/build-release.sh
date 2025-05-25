#!/bin/bash

# Multi-Environment Build Script for Karage App
# This script builds, signs, and prepares your app for different environments

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
ENVIRONMENT=${1:-"prod"}
BUILD_TYPE=${2:-"release"}

echo -e "${BLUE}🚀 Building Karage App${NC}"
echo -e "${BLUE}Environment: ${ENVIRONMENT}${NC}"
echo -e "${BLUE}Build Type: ${BUILD_TYPE}${NC}"
echo "============================================="
echo ""

# Validate inputs
if [[ ! "$ENVIRONMENT" =~ ^(dev|prod)$ ]]; then
    echo -e "${RED}❌ Invalid environment. Use 'dev' or 'prod'${NC}"
    exit 1
fi

if [[ ! "$BUILD_TYPE" =~ ^(debug|staging|release)$ ]]; then
    echo -e "${RED}❌ Invalid build type. Use 'debug', 'staging', or 'release'${NC}"
    exit 1
fi

# Check for invalid combinations
if [[ "$ENVIRONMENT" == "dev" && "$BUILD_TYPE" == "release" ]]; then
    echo -e "${RED}❌ Invalid combination: dev environment with release build type${NC}"
    echo -e "${YELLOW}💡 Suggestion: Use 'dev debug' or 'prod release'${NC}"
    exit 1
fi

# Create build variant name
BUILD_VARIANT="${ENVIRONMENT}$(echo ${BUILD_TYPE:0:1} | tr '[:lower:]' '[:upper:]')${BUILD_TYPE:1}"

echo -e "${BLUE}📦 Build variant: ${BUILD_VARIANT}${NC}"
echo ""

# Step 1: Clean previous builds
echo -e "${BLUE}📦 Cleaning previous builds...${NC}"
./gradlew clean

# Step 2: Run tests (only for release builds)
if [[ "$BUILD_TYPE" == "release" || "$BUILD_TYPE" == "staging" ]]; then
    echo -e "${BLUE}🧪 Running tests...${NC}"
    ./gradlew test
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Tests failed! Fix tests before release.${NC}"
        exit 1
    fi

    # Step 3: Run lint checks
    echo -e "${BLUE}🔍 Running lint checks...${NC}"
    ./gradlew lint
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Lint checks failed! Fix lint issues before release.${NC}"
        exit 1
    fi

    # Step 4: Run static analysis
    echo -e "${BLUE}📊 Running static analysis...${NC}"
    ./gradlew detekt
fi

# Step 5: Build APK
echo -e "${BLUE}🔨 Building ${BUILD_VARIANT} APK...${NC}"
./gradlew assemble${BUILD_VARIANT}

# Step 6: Verify APK
echo -e "${BLUE}✅ Verifying APK...${NC}"
APK_PATH="app/build/outputs/apk/${ENVIRONMENT}/${BUILD_TYPE}/app-${ENVIRONMENT}-${BUILD_TYPE}.apk"

if [ ! -f "$APK_PATH" ]; then
    echo -e "${RED}❌ APK not found at $APK_PATH${NC}"
    echo -e "${YELLOW}🔍 Checking available APKs:${NC}"
    find app/build/outputs/apk -name "*.apk" -type f
    exit 1
fi

# Check APK size
APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
echo -e "${GREEN}📱 APK built successfully!${NC}"
echo -e "${GREEN}📏 APK size: $APK_SIZE${NC}"
echo -e "${GREEN}📁 Location: $APK_PATH${NC}"

# Step 7: Generate APK info
echo -e "${BLUE}📋 APK Information:${NC}"
if command -v aapt &> /dev/null; then
    aapt dump badging "$APK_PATH" | grep -E "(package|application-label|uses-permission|uses-feature)" | head -10
else
    echo -e "${YELLOW}⚠️  aapt not found, skipping APK analysis${NC}"
fi

echo ""
echo -e "${GREEN}✅ ${ENVIRONMENT} ${BUILD_TYPE} build completed successfully!${NC}"
echo ""
echo -e "${YELLOW}📤 Next steps for ${BUILD_TYPE} builds:${NC}"

if [[ "$BUILD_TYPE" == "debug" ]]; then
    echo -e "${YELLOW}   • Install on device: adb install -r '$APK_PATH'${NC}"
    echo -e "${YELLOW}   • Test app functionality${NC}"
    echo -e "${YELLOW}   • Use for development and debugging${NC}"
elif [[ "$BUILD_TYPE" == "staging" ]]; then
    echo -e "${YELLOW}   • Test on multiple devices${NC}"
    echo -e "${YELLOW}   • Share with QA team${NC}"
    echo -e "${YELLOW}   • Perform integration tests${NC}"
elif [[ "$BUILD_TYPE" == "release" ]]; then
    echo -e "${YELLOW}   • Upload to Google Play Console for testing${NC}"
    echo -e "${YELLOW}   • Create a release in your Git repository${NC}"
    echo -e "${YELLOW}   • Deploy via GitHub Actions workflow${NC}"
    echo -e "${YELLOW}   • Distribute to production users${NC}"
fi

echo ""
echo -e "${BLUE}🔧 Build Commands:${NC}"
echo -e "${BLUE}   • Dev Debug:     ./scripts/build-release.sh dev debug${NC}"
echo -e "${BLUE}   • Dev Staging:   ./scripts/build-release.sh dev staging${NC}"
echo -e "${BLUE}   • Prod Staging:  ./scripts/build-release.sh prod staging${NC}"
echo -e "${BLUE}   • Prod Release:  ./scripts/build-release.sh prod release${NC}"
