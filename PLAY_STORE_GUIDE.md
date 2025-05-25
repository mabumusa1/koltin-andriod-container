# Google Play Store Release Guide

## Prerequisites for Play Store Release

### 1. Google Play Console Account
- Create a Google Play Console account ($25 one-time fee)
- Complete developer account verification
- Accept Play Console Developer Distribution Agreement

### 2. App Store Listing Preparation

#### Required Assets:
- **App Icon**: 512x512 PNG (high-res icon)
- **Feature Graphic**: 1024x500 PNG/JPEG
- **Screenshots**: 
  - Phone: 2-8 screenshots (16:9 or 9:16 ratio)
  - Tablet: 1-8 screenshots (optional but recommended)
- **Short Description**: Up to 80 characters
- **Full Description**: Up to 4000 characters
- **Privacy Policy URL**: Required for apps

#### Content Rating:
- Complete the content rating questionnaire
- Get your app rated by International Age Rating Coalition (IARC)

### 3. Technical Requirements

#### App Requirements:
- Target API level 34 (Android 14) or higher
- Signed with upload key
- Optimized APK or App Bundle (AAB)
- 64-bit native code (if using native libraries)

#### Privacy & Security:
- Privacy Policy (required)
- Data safety form completion
- Permissions justification
- Security review (if using sensitive permissions)

## Release Process

### Step 1: Create App in Play Console
1. Go to Google Play Console
2. Create new app
3. Fill in app details:
   - App name: "Karage Calculator"
   - Default language: English (US)
   - App or game: App
   - Free or paid: Free

### Step 2: Upload APK/AAB
1. Go to Release > Production
2. Create new release
3. Upload your signed APK or AAB
4. Add release notes

### Step 3: Complete Store Listing
1. Main store listing
2. Graphic assets
3. Categorization
4. Contact details
5. Privacy policy

### Step 4: Content Rating
1. Complete questionnaire
2. Review and apply rating

### Step 5: App Pricing & Distribution
1. Set countries/regions
2. Set pricing (free/paid)
3. Device categories

### Step 6: Review and Publish
1. Review all sections for completeness
2. Submit for review
3. Wait for approval (usually 1-3 days)

## Alternative Distribution Options

### 1. Direct APK Distribution
- Host APK on your website
- Send via email/messaging
- Use QR codes for easy download
- ⚠️ Users need to enable "Unknown sources"

### 2. Alternative App Stores
- Amazon Appstore
- Samsung Galaxy Store
- F-Droid (for open source apps)
- Huawei AppGallery

### 3. Enterprise Distribution
- Internal testing via Play Console
- Firebase App Distribution
- Company MDM solutions

## App Bundle vs APK

### Recommended: App Bundle (AAB)
- Smaller download size
- Dynamic delivery
- Better optimization
- Required for new apps on Play Store

### APK (Traditional)
- Universal compatibility
- Direct installation
- Larger file size
- Easier for side-loading

## Testing Before Release

### Internal Testing
- Upload to Play Console internal testing
- Share with team members
- Quick review process

### Closed Alpha/Beta Testing
- Limited user groups
- Collect feedback
- Test different devices/Android versions

### Open Beta Testing
- Public beta program
- Larger user base
- Pre-launch feedback
