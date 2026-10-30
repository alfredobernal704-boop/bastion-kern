# Firebase app registration audit — 30 October 2026

## Source: configuration files provided by project owner

Both the submitted Android and iOS configuration files identify Firebase
project `bastion-sdk` (project number `158994318096`).

| Platform | Registered in supplied configuration | Active client source code | Match |
| --- | --- | --- | --- |
| Android | `Bastionkern.com` | `app.bastionkern` | **No** |
| iOS | `Bastionkern.offentlogspot.com` | SwiftUI target not yet created | Not yet tested |

This is a **case-sensitive and exact-identity mismatch**, not a problem with
the Firebase project. Neither the current Android JSON nor the iOS plist
should be copied into the Android module. Do not rename the Android package
to match the incorrectly registered app.

## Correct registration, keeping the same Firebase project

1. Open the existing Firebase project:
   https://console.firebase.google.com/project/bastion-sdk/settings/general
2. In **Your apps**, select **Add app** > **Android**. Register the Android
   package name **`app.bastionkern`** and a distinct nickname, e.g.
   **BASTION-KERN Android**. Do not delete or rename the already registered
   `Bastionkern.com` app: Firebase cannot change a registered app's package
   identifier, and other software may already depend on it.
3. Obtain the new app's **google-services.json** for `app.bastionkern`.
   An authenticated Firebase CLI session can retrieve it directly:

   ```bash
   npx -y firebase-tools@latest apps:list ANDROID --project bastion-sdk
   npx -y firebase-tools@latest apps:sdkconfig ANDROID <NEW_ANDROID_APP_ID> --project bastion-sdk
   ```

4. Verify the new JSON has a client with:
   `client_info.android_client_info.package_name == "app.bastionkern"`.
   The app ID must belong to project number `158994318096`.
   Use `python3 android/scripts/verify_firebase_config.py google-services.json`.
5. Install the file in `android/app/google-services.json` for local builds
   **or** supply it to GitHub Actions using the
   `FIREBASE_GOOGLE_SERVICES_JSON_BASE64` secret (base64-encoded UTF-8).
   Keep the secret scoped to the repository. Do not publish the file as part
   of a token or service-account credential.
6. Enable **Authentication > Sign-in method > Email/Password** in
   https://console.firebase.google.com/project/bastion-sdk/authentication/providers.
   If it is already enabled, do not create a duplicate provider.
7. Review the Firestore database's edition and existing rules before creating
   or deploying anything; neither the provided JSON nor plist establishes that
   the database is configured. The repository's Firestore rules remain a
   **prototype, not remotely deployed**.

## iOS configuration

The supplied `GoogleService-Info.plist.xml` identifies a previously
registered iOS Firebase app with bundle ID
`Bastionkern.offentlogspot.com`. Its project and sender identifiers agree
with the Android config. Before using that plist in Xcode:

- Confirm the SwiftUI target's **Bundle Identifier** is exactly
  `Bastionkern.offentlogspot.com` (including capitalization).
- In the actual Xcode project, name the config `GoogleService-Info.plist`,
  include it in the application target, then initialise Firebase according to
  its iOS SDK guidance.
- Do not assume iOS code exists or an iOS application has been compiled.
- The repository includes `android/scripts/verify_firebase_ios_config.py`
  for validating the supplied plist format and registered identifiers before
  iOS setup; no keys from the plist are stored in the repository.

## Why the Android file was not committed

The Google Services Gradle plugin searches the downloaded JSON for a client
whose package name matches `applicationId`. The submitted Android file lacks
that client. The build therefore cannot activate Firebase Auth or Firestore
with this file. The existing successful APK is a Mapbox-capable build with
account operations disabled pending registered Firebase configuration.

No Firebase Console mutation, app registration, Authentication provider
change or Cloud Firestore rules deployment has been executed by this audit.

References:
- https://firebase.google.com/docs/android/setup
- https://firebase.google.com/docs/ai-assistance/agent-skills
- https://firebase.google.com/docs/auth/android/password-auth
- https://firebase.google.com/docs/firestore/security/get-started
