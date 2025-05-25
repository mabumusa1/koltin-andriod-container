# Release Process Documentation

## Overview

This project now follows a comprehensive release process with proper versioning, file naming conventions, and support for both staging release candidates and production releases.

## Release Workflows

### 1. Main Release Workflow (`release.yml`)
- **Triggers**: Manual dispatch, tags (v*), and branch pushes (prod/staging)
- **Purpose**: Flexible workflow for various build types and environments
- **Features**: Automatic tag creation, proper file naming, release generation

### 2. Staging Release Candidate Workflow (`release-staging.yml`)
- **Trigger**: Manual dispatch only
- **Purpose**: Create staging release candidates for testing
- **Output**: Creates `v{version}-rc` tags and pre-release GitHub releases

### 3. Production Release Workflow (`release-production.yml`)
- **Trigger**: Manual dispatch only
- **Purpose**: Create production releases after staging validation
- **Output**: Creates `v{version}` tags and production GitHub releases

## File Naming Conventions

### APK Files
- **Staging RC**: `karage-app-{version}-rc-staging-staging.apk`
- **Production**: `karage-app-{version}-prod-release.apk`
- **Debug builds**: `karage-app-{version}-{env}-debug.apk`

### Examples
- `karage-app-1.2.0-rc-staging-staging.apk` (Staging RC)
- `karage-app-1.2.0-prod-release.apk` (Production release)

## Tag Naming Conventions

### Staging Release Candidates
- **Format**: `v{version}-rc`
- **Example**: `v1.2.0-rc`

### Production Releases
- **Format**: `v{version}`
- **Example**: `v1.2.0`

## Release Process Flow

### For Staging Release Candidates

1. **Create Staging RC**:
   ```
   Navigate to Actions → Staging Release (Release Candidate)
   Input version: 1.2.0
   Enable "Create GitHub Release": true
   ```

2. **Outputs**:
   - Tag: `v1.2.0-rc`
   - APK: `karage-app-1.2.0-rc-staging-staging.apk`
   - GitHub Release: Draft, Pre-release

3. **Testing**:
   - Download and test the RC APK
   - Verify all functionality
   - Report any issues

### For Production Releases

1. **Create Production Release**:
   ```
   Navigate to Actions → Production Release
   Input version: 1.2.0
   Input staging_tag: v1.2.0-rc (optional but recommended)
   Enable "Create GitHub Release": true
   ```

2. **Outputs**:
   - Tag: `v1.2.0`
   - APK: `karage-app-1.2.0-prod-release.apk`
   - GitHub Release: Published, Not pre-release

## Best Practices

### 1. Version Numbering
- Follow semantic versioning: `MAJOR.MINOR.PATCH`
- Examples: `1.0.0`, `1.2.3`, `2.0.0`

### 2. Release Candidate Testing
- Always create and test staging RCs before production
- Use the staging_tag parameter in production workflow for traceability

### 3. Release Notes
- Update release notes in the workflow files
- Include meaningful changelog information
- Highlight breaking changes

### 4. Quality Gates
- Production releases should only be created after staging RC approval
- The production workflow validates staging RC existence (if provided)
- Never overwrite existing production tags

## Workflow Features

### Tag Management
- **Staging**: Can overwrite existing RC tags for iterative testing
- **Production**: Prevents overwriting existing production tags
- **Automatic**: Creates tags with proper naming conventions

### APK Signing
- All release and staging builds are automatically signed
- Debug builds remain unsigned for development
- APK verification ensures integrity

### Release Creation
- **Staging**: Creates draft, pre-release GitHub releases
- **Production**: Creates published, stable GitHub releases
- **Customizable**: Option to skip release creation if only APK is needed

### Artifact Management
- Proper APK renaming with version and environment info
- Organized artifact uploads with descriptive names
- Retention follows GitHub's artifact policies

## Troubleshooting

### Common Issues

1. **Tag Already Exists** (Production):
   - Production tags cannot be overwritten
   - Use a different version number
   - Delete the existing tag manually if needed

2. **Missing Staging RC**:
   - Create and test staging RC first
   - Provide staging_tag parameter for traceability

3. **Build Failures**:
   - Check Gradle build configuration
   - Verify signing secrets are properly configured
   - Review build logs for specific errors

### Required Secrets

Ensure these GitHub secrets are configured:
- `SIGNING_KEY`: Base64 encoded keystore
- `KEY_STORE_PASSWORD`: Keystore password
- `KEY_ALIAS`: Key alias
- `KEY_PASSWORD`: Key password
- `GITHUB_TOKEN`: Automatically provided by GitHub

## Migration from Old Process

If migrating from the previous release process:

1. **Update Documentation**: Review and update any existing release documentation
2. **Team Training**: Ensure team understands new workflow structure
3. **Tool Updates**: Update any automated tools that reference old artifact names
4. **Cleanup**: Archive or remove old workflow files if not needed

## Future Enhancements

Potential improvements to consider:

1. **Automated Testing**: Integration with test suites before release creation
2. **Play Store Upload**: Automatic upload to Google Play Console
3. **Slack/Teams Notifications**: Notify team of new releases
4. **Changelog Generation**: Automatic changelog from commits
5. **Release Approval**: Manual approval gates for production releases
