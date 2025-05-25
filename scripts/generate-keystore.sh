#!/bin/bash

# Script to generate Android release keystore
# This keystore will be used to sign your release APKs

echo "🔐 Generating Android Release Keystore"
echo "======================================"
echo ""
echo "⚠️  IMPORTANT: Keep this keystore and passwords safe!"
echo "   You'll need them for ALL future releases of your app."
echo "   If you lose this keystore, you cannot update your app on Play Store."
echo ""

# Create keystore directory
mkdir -p release

# Generate keystore
keytool -genkey -v \
    -keystore release/karage-app-keystore.jks \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -alias karage-app-key \
    -dname "CN=Karage App, OU=Development, O=Karage, L=City, S=State, C=US" \
    -storepass android123 \
    -keypass android123

echo ""
echo "✅ Keystore generated successfully!"
echo "📁 Location: release/karage-app-keystore.jks"
echo "🔑 Alias: karage-app-key"
echo "🔒 Store Password: android123"
echo "🔒 Key Password: android123"
echo ""
echo "📋 For GitHub Secrets, you'll need to:"
echo "   1. Convert keystore to base64: base64 -w 0 release/karage-app-keystore.jks"
echo "   2. Add the base64 string as SIGNING_KEY secret"
echo "   3. Add other secrets as shown below"
