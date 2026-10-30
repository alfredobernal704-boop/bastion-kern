plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// The release APK can be built without the Firebase config while project registration
// is pending. Once google-services.json is present, apply the official Firebase plugin.
// An unconfigured app explicitly disables account operations rather than using a
// guessed Firebase API key or silently connecting to another Firebase project.
if (file("google-services.json").isFile) {
    apply(plugin = "com.google.gms.google-services")
}

android {
    namespace = "app.bastionkern"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.bastionkern"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resValue(
            "string", "mapbox_access_token",
            providers.gradleProperty("MAPBOX_PUBLIC_TOKEN")
                .orElse(providers.environmentVariable("MAPBOX_PUBLIC_TOKEN"))
                .orElse("").get()
        )
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures { compose = true }
}
dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("com.mapbox.maps:android-ndk27:11.34.1")
    implementation("com.mapbox.extension:maps-compose-ndk27:11.34.1")

    implementation(platform("com.google.firebase:firebase-bom:35.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20240303")
}
