# BASTION-KERN — Android first-mobile prototype

**Status:** Source prototype; not yet built on an Android device. This module is additive to the existing website and does not modify it.

## Implemented

- Android native Kotlin/Jetpack Compose entry point, dark UI based on the supplied Lovable references.
- Mapbox Standard 3D globe and Standard Satellite map-style switch.
- Real USGS M4.5+ past-day GeoJSON feed, fetched over HTTPS on IO dispatcher.
- Point annotations and event detail sheet with source status, timestamp, depth and official URL.
- Explicit token-missing, loading, empty and network-error states.
- JVM unit tests for input validation and source-URL restrictions.

This first milestone does **not** implement earthquake prediction, ShakeAlert, full-screen push alerts, automated rescue routing, Scythe AI, or live user geolocation. The USGS feed is a **reported-event feed**; do not call it earthquake early warning.

## Build

Open `android/` as an Android Studio project. Use JDK 17, Android SDK 35, and Gradle 8.9 (e.g. `gradle wrapper --gradle-version 8.9` after installing Gradle).

Add to your **user-specific** `~/.gradle/gradle.properties` (never commit these):

```properties
MAPBOX_PUBLIC_TOKEN=pk.your_public_token
MAPBOX_DOWNLOADS_TOKEN=sk.your_token_with_DOWNLOADS_READ_scope
```

The `MAPBOX_PUBLIC_TOKEN` is injected as Android `mapbox_access_token` by Gradle. The download token is used **only by Gradle** to access Mapbox Maven. Do not place a secret token in app resources or source files.

Commands: `gradle testDebugUnitTest`, `gradle assembleDebug`. Install the generated `app/build/outputs/apk/debug/app-debug.apk` onto an Android device with Google Play services and OpenGL ES 3 support.

## SDK choice

Mapbox Maps SDK and Compose extension are both pinned to 11.34.1, NDK27 variants for 16 KB page-size support. Source reference:
https://docs.mapbox.com/android/maps/guides/install/

## Iteration backlog

1. Add 2D navigation mode with official Navigation SDK (check compatible Navigation SDK release).
2. Design event ingestion service with provider IDs, revision tracking, PostGIS and verification.
3. User permission-driven location and geofencing; viewport retrieval and clustering.
4. Push via FCM: explicit policies and Android 14+ full-screen restrictions.
5. Scythe AI tool-using backend, retrieval citations and latency metrics.
6. Performance benchmarks: loading, zoom changes, frame timing, thermal behavior, low battery.

## UI references

The supplied 28 Lovable screenshots and interactive alert HTML were used as visual design references. The UI uses muted navy surfaces and gold accents and avoids excessive glow or continuously running shaders.
