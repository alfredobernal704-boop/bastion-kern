# Firebase integration — BASTION-KERN Android

Target project: **bastion-sdk**. Android application ID: **app.bastionkern**.
This is an additive client implementation. **Cloud registration, the Email/Password
provider and the Firestore database must be confirmed in the actual Firebase project**.
This branch does not create cloud resources or deploy security rules.

## Implementation

- Google Services Gradle plugin `4.5.0` (applied when the official
  `android/app/google-services.json` file exists).
- Firebase Android BoM `35.1.0`, non-KTX Auth and Firestore main modules.
- Account bottom sheet: sign up, sign in, sign out, password reset and email verification.
- Per-user Firestore document `users/{uid}` with private display name and server timestamps.
- Prototype Security Rules: user-owned get/create/update, no enumeration,
  no client delete, no public reads, no privilege fields. **Not yet deployed**.
- The geospatial map remains accessible to Test Lab without Firebase configured;
  account actions show a clear configuration message instead of crashing.

## Register the app in the existing project

Use the official Firebase agent skills `firebase-basics`, `firebase-auth-basics`,
`firebase-firestore` and `firestore-rules-creation`.

1. Authenticate the Firebase CLI, verify the project is **bastion-sdk**:
   `npx -y firebase-tools@latest projects:list`
2. Check whether `app.bastionkern` is registered:
   `npx -y firebase-tools@latest apps:list ANDROID --project bastion-sdk`
3. **Only if not registered**, run:
   `npx -y firebase-tools@latest apps:create ANDROID "BASTION-KERN Android" --package-name app.bastionkern --project bastion-sdk`
4. Retrieve the app ID from the result or apps:list, then run:
   `npx -y firebase-tools@latest apps:sdkconfig ANDROID <FIREBASE_ANDROID_APP_ID> --project bastion-sdk`
   Save the exact JSON as `android/app/google-services.json`.
   Do not fabricate or manually substitute a Firebase app ID.
5. In the Firebase Console for **bastion-sdk**, open **Authentication >
   Sign-in method > Email/Password** and enable Email/Password.
6. Check Firestore databases and edition:
   `npx -y firebase-tools@latest firestore:databases:list --project bastion-sdk`
   Inspect the chosen database's edition before provisioning.
   **Do not create a new database if one already exists without inspecting it.**
7. For a compatible **Standard** edition default database, review and
   test `firebase/firestore.rules` in the Emulator Suite, then deploy:
   `npx -y firebase-tools@latest deploy --only firestore:rules,firestore:indexes --project bastion-sdk`
   Do not deploy these rules to a different edition or existing production
   schema without review. Rule deployment can replace the existing ruleset.

## GitHub Actions injection

Store the UTF-8 `google-services.json` in the repository Actions secret
`FIREBASE_GOOGLE_SERVICES_JSON_BASE64`, encoded with Base64 (not encryption).
The Android workflow will decode it into `android/app/google-services.json`
only for the build and validate the exact project ID and package name.
Do not send the file or passwords in chat. Firebase's Android config is not a
secret, but excluding it from commits avoids accidental project mixing.

The `MAPBOX_DOWNLOADS_TOKEN` and `MAPBOX_PUBLIC_TOKEN` setup remains unchanged.

## Tests still required

- Android CI compile WITH real Firebase JSON (not yet complete).
- Firebase Test Lab on Android physical phone: login, registration, reset email.
- Firestore rules emulator: owner can read/edit only own profile; cross-user,
  collection queries and privilege escalation are rejected.
- Verify that Firebase project, collection region, edition and provider status are real.
- Never treat debug builds as production; add App Check and abuse mitigations
  before exposing paid or sensitive features.

Official references:
https://firebase.google.com/docs/android/setup
https://firebase.google.com/docs/auth/android/start
https://firebase.google.com/docs/firestore/security/get-started
https://firebase.google.com/docs/ai-assistance/agent-skills
