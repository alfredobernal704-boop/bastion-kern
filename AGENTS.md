# BASTION-KERN engineering agent instructions

## Firebase is the selected mobile backend

Use the **existing** Firebase project `bastion-sdk`. Android application ID:
`app.bastionkern`. Never create an alternative project, invent a Firebase app
ID, migrate to another backend, or modify Firestore on another project.

**For every Firebase task**, first locate and follow the applicable Google
Firebase Agent Skills from https://firebase.google.com/docs/ai-assistance/agent-skills
and https://github.com/firebase/agent-skills:

- `firebase-basics` for CLI authentication, existing project selection and Android app registration.
- `firebase-auth-basics` for Email/Password provider and user session flows.
- `firebase-firestore` for database edition detection, data model and Android SDK use.
- `firestore-rules-creation` (or authorised rules subagent) for all security rules authoring.
- `firestore-security-rules-auditor` if available for final rule review.

Use the project’s actual authenticated Firebase MCP connection or Firebase CLI.
If neither is connected, prepare code but **report registration, provider
activation, database provisioning and rules deployment as pending**.

## Security and integration boundaries

- Mapbox Maven `sk.*` credentials must only exist in GitHub Actions secrets
  or private Gradle properties; never embed them in an APK.
- `google-services.json` comes from the registered Android app; never fake its values.
- `firebase/firestore.rules` is a **prototype for a Standard/default database**.
  Detect Firestore edition and review existing rules before any deployment.
- Protect `users/{uid}`: no public profile data, self-assigned roles, paid
  entitlements, identity verification bypass or ownership changes.
- User passwords are only handled by Firebase Auth; never store them in Firestore.
- Distinguish USGS reported earthquake records from earthquake early warning.
- Preserve the unrelated public website and the main branch until CI validation.
