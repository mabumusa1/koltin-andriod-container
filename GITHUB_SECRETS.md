# GitHub Secrets Configuration for Android Release

To configure your GitHub repository for automated releases, you need to add the following secrets:

## Required Secrets

### 1. SIGNING_KEY
- **Description**: Base64 encoded keystore file
- **How to get**: Run this command in your terminal:
  ```bash
  base64 -w 0 release/karage-app-keystore.jks
  ```
- **Value**: Copy the entire base64 string output

### 2. KEY_ALIAS
- **Description**: Keystore alias name
- **Value**: `karage-app-key`

### 3. KEY_STORE_PASSWORD
- **Description**: Keystore password
- **Value**: `android123`

### 4. KEY_PASSWORD
- **Description**: Key password (same as store password in our case)
- **Value**: `android123`

## How to Add Secrets to GitHub

1. Go to your GitHub repository
2. Click on **Settings** tab
3. Click on **Secrets and variables** > **Actions**
4. Click **New repository secret**
5. Add each secret with the name and value listed above

## Security Notes

⚠️ **IMPORTANT**: In a real production environment:
- Use strong, unique passwords for your keystore
- Store the keystore file securely offline
- Consider using GitHub's encrypted secrets for sensitive data
- Never commit the keystore file to your repository

## Testing the Workflow

After adding the secrets, you can test the workflow by:
1. Pushing to the `prod` branch
2. Creating a git tag (e.g., `v1.0.0`)
3. Manually triggering the workflow from GitHub Actions

## Workflow Triggers

Your current workflow triggers on:
- Push to `prod` branch
- Push to tags starting with `v*`
- Manual workflow dispatch
