import org.gradle.authentication.http.BasicAuthentication

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "MapboxDownloads"
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                // The username is fixed by Mapbox; this is not the Mapbox account name.
                username = "mapbox"
                // The sk.* secret must never be added to application resources or committed.
                password = providers.gradleProperty("MAPBOX_DOWNLOADS_TOKEN")
                    .orElse(providers.environmentVariable("MAPBOX_DOWNLOADS_TOKEN"))
                    .orElse("").get()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            content {
                includeGroupByRegex("com\\.mapbox(\\..*)?")
            }
        }
    }
}

rootProject.name = "BASTION-KERN-Android"
include(":app")
