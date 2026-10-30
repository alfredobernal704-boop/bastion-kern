# Mapbox Maven authenticated compilation: BASTION-KERN Android

## Authentication boundary

Mapbox provides Maps SDK dependencies at `https://api.mapbox.com/downloads/v2/releases/maven`. The publisher requires HTTP Basic authentication:

- username: `mapbox` (fixed, never your Mapbox account username)
- password: a secret access token beginning with `sk.` and `Downloads:Read` permission.

This credential authorises **SDK downloads**. It must remain server-side (Gradle CI or developer machine); **do not put it in Android resources, manifest, app code or a git commit**.

The `MAPBOX_PUBLIC_TOKEN` value begins with `pk.` and is used by the built Android application to request map resources. It is not an SDK download credential.

## Enable GitHub Actions (one-time owner operation)

1. In the Mapbox dashboard (https://account.mapbox.com/access-tokens/), create a new secret token with the `Downloads:Read` scope. Limit other scopes to those actually needed.
2. In the GitHub repository `alfredobernal704-boop/bastion-kern`, open **Settings > Secrets and variables > Actions**.
3. Under **Repository secrets**, create `MAPBOX_DOWNLOADS_TOKEN` and paste the `sk.*` token directly in the GitHub UI.
4. Under **Repository variables**, create `MAPBOX_PUBLIC_TOKEN` with an appropriate `pk.*` token (subject to domain/URL restrictions compatible with native Android).
5. Go to **Actions > Android first-mobile prototype > Run workflow** and choose the feature branch.
6. Confirm log step **Verify Mapbox Maven authentication and SDK artefact** reports HTTP 200, then the Gradle test / assemble steps pass and the debug APK artifact is published.

## Current CI behaviour

The workflow explicitly **fails** on missing or incorrect token prefixes. This prevents a false-green build from skipped Gradle steps. A second preflight makes an authenticated HTTP request to the exact POM for `com.mapbox.maps:android-ndk27:11.34.1`. HTTP 200 validates access to this Maven artifact, but **does not** prove the complete APK builds; the subsequent Gradle step does.

## Troubleshooting

- HTTP 401: invalid, expired or revoked `sk.*` token, or incorrect Basic auth.
- HTTP 403: credential missing `Downloads:Read` scope or account restriction.
- HTTP 404: artifact/version path incorrect; check current Mapbox release coordinates.
- Maven dependency resolution failure after preflight: inspect transitive dependency failures, Gradle module versions and repository restrictions.
- Token preflight success but map screen fails: verify public token `pk.*` and its resource scope.
- For a local Android Studio build, configure both values in `~/.gradle/gradle.properties`; never commit that file.

Official docs:
- https://docs.mapbox.com/android/maps/guides/install/
- https://docs.mapbox.com/android/navigation/ux/guides/install/

GitHub connector limitations: it cannot write Actions repository secrets or read the Mapbox account's private token management page. The owner must add tokens directly via the GitHub interface, or use a tool explicitly authorised for secrets management. The secret is not available through geospatial Mapbox API tool calls.
