# Publishing AuthUI to Maven Central

This guide explains how to publish the AuthUI library to Maven Central (Sonatype).

## Prerequisites

Before publishing, you need:

1. **Sonatype Account**
   - Create an account at https://issues.sonatype.org/
   - Create a JIRA ticket to claim your groupId `io.github.fahmihidayah`
   - Follow: https://central.sonatype.org/register/central-portal/

2. **GPG Key for Signing**
   - Install GPG: `brew install gnupg` (macOS) or download from https://gnupg.org/
   - Generate a key pair
   - Publish your public key to a key server

3. **GitHub Repository**
   - Update the repository URL in `build.gradle.kts` if different from `https://github.com/fahmihidayah/authui`
   - Ensure your code is pushed to GitHub

## Step 1: Generate GPG Key

```bash
# Generate a new GPG key
gpg --gen-key

# Follow the prompts:
# - Real name: Your Name
# - Email: your-email@example.com
# - Password: Choose a strong password

# List your keys to get the key ID
gpg --list-keys

# You'll see output like:
# pub   rsa3072 2024-01-01 [SC]
#       ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
# The long string is your key ID

# Export your public key to a key server
gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
```

## Step 2: Configure Gradle Properties

Create or update `~/.gradle/gradle.properties` with your credentials:

```properties
# Sonatype credentials
ossrhUsername=your-sonatype-username
ossrhPassword=your-sonatype-password

# GPG signing configuration
signing.keyId=QRST7890
# Use the last 8 characters of your key ID
signing.password=your-gpg-password
signing.secretKeyRingFile=/Users/yourusername/.gnupg/secring.gpg
```

### Export Secret Key Ring (for older GPG versions)

If you're using GPG 2.1+, you need to export your secret key:

```bash
# Export secret key
gpg --export-secret-keys -o ~/.gnupg/secring.gpg
```

Alternatively, use GPG agent (recommended):

```properties
# In ~/.gradle/gradle.properties
signing.gnupg.executable=gpg
signing.gnupg.keyName=ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
```

And update `authui/build.gradle.kts` signing block:

```kotlin
signing {
    useGpgCmd()
    sign(publishing.publications["release"])
}
```

## Step 3: Update POM Information

Edit `/Users/s/Documents/AndroidStudioProjects/AuthUi/authui/build.gradle.kts`:

1. Update the developer email at line 105:
   ```kotlin
   email.set("your-actual-email@example.com")
   ```

2. Update repository URLs if needed (lines 92, 110-112)

3. Verify the version number (line 76 and 83):
   ```kotlin
   version = "0.1.0"
   ```

## Step 4: Build and Publish

### Test the Build

```bash
cd /Users/s/Documents/AndroidStudioProjects/AuthUi

# Clean build
./gradlew clean

# Build the library
./gradlew :authui:assembleRelease

# Generate POM and artifacts
./gradlew :authui:generatePomFileForReleasePublication
```

### Publish to Maven Local (Test)

```bash
# Publish to local Maven repository to test
./gradlew :authui:publishToMavenLocal

# Check ~/.m2/repository/io/github/fahmihidayah/authui/0.1.0/
```

### Publish to Maven Central

```bash
# Publish to Sonatype (Maven Central)
./gradlew :authui:publish

# Or use the publish task with signing
./gradlew :authui:publishReleasePublicationToSonatypeRepository
```

## Step 5: Release on Sonatype

1. Log in to https://s01.oss.sonatype.org/
2. Click on "Staging Repositories" in the left sidebar
3. Find your repository (usually `io.github.fahmihidayah-XXXX`)
4. Select it and click "Close" button
   - Sonatype will run validation checks
   - This may take a few minutes
5. Once validation passes, click "Release" button
6. The artifact will be synced to Maven Central within 10-30 minutes
7. It will be searchable at https://central.sonatype.com within 2 hours

## Step 6: Verify Publication

After release, verify your library is available:

```bash
# Check Maven Central search
# Visit: https://central.sonatype.com/artifact/io.github.fahmihidayah/authui

# Or use in a project
dependencies {
    implementation("io.github.fahmihidayah:authui:0.1.0")
}
```

## Common Issues

### Issue: "No secring.gpg file"

**Solution**: Export your secret key or use GPG agent:
```bash
gpg --export-secret-keys -o ~/.gnupg/secring.gpg
```

Or configure to use GPG agent in `gradle.properties`:
```properties
signing.gnupg.executable=gpg
signing.gnupg.keyName=YOUR_KEY_ID
```

### Issue: "401 Unauthorized" when publishing

**Solution**:
- Verify your Sonatype credentials in `~/.gradle/gradle.properties`
- Ensure your JIRA ticket for groupId is approved
- Check you're using the correct repository URL (s01 vs old oss)

### Issue: "Could not find method signing()"

**Solution**: Ensure the signing plugin is applied:
```kotlin
plugins {
    id("signing")
}
```

### Issue: "Publication only contains dependencies and/or constraints"

**Solution**: This is already handled with:
```kotlin
afterEvaluate {
    from(components["release"])
}
```

Make sure you're building the release variant.

## Version Updates

To publish a new version:

1. Update version in `authui/build.gradle.kts`:
   ```kotlin
   version = "0.2.0"  // Lines 76 and 83
   ```

2. Update the version in the publication block as well

3. Build and publish again:
   ```bash
   ./gradlew clean :authui:publish
   ```

## CI/CD with GitHub Actions

Create `.github/workflows/publish.yml`:

```yaml
name: Publish to Maven Central

on:
  release:
    types: [created]

jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'

      - name: Setup Gradle
        uses: gradle/gradle-build-action@v2

      - name: Publish to Maven Central
        run: ./gradlew :authui:publish
        env:
          OSSRH_USERNAME: ${{ secrets.OSSRH_USERNAME }}
          OSSRH_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
          SIGNING_KEY_ID: ${{ secrets.SIGNING_KEY_ID }}
          SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}
          SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
```

Add secrets in GitHub repository settings:
- `OSSRH_USERNAME`
- `OSSRH_PASSWORD`
- `SIGNING_KEY_ID`
- `SIGNING_PASSWORD`
- `SIGNING_KEY` (base64 encoded GPG key)

## Resources

- [Maven Central Publishing Guide](https://central.sonatype.org/publish/publish-guide/)
- [Requirements](https://central.sonatype.org/publish/requirements/)
- [GPG Signing](https://central.sonatype.org/publish/requirements/gpg/)
- [Sonatype JIRA](https://issues.sonatype.org/)

## Summary

Your library configuration is complete. To publish:

1. ✅ Module converted to library
2. ✅ Maven publish plugin configured
3. ✅ POM metadata set up
4. ✅ Signing configured
5. ⚠️  Update developer email in build.gradle.kts
6. ⚠️  Create Sonatype account and claim groupId
7. ⚠️  Generate and publish GPG key
8. ⚠️  Configure gradle.properties with credentials
9. ⚠️  Run `./gradlew :authui:publish`
10. ⚠️  Release on Sonatype web interface

After completing steps 6-10, your library will be available at:
```
io.github.fahmihidayah:authui:0.1.0
```
