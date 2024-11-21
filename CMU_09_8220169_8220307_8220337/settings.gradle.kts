import org.gradle.internal.impldep.org.jsoup.safety.Safelist.basic

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
//            authentication {
//                basic(BasicAuthentication)
//            }
//            credentials {
//                // Do not change the username below.
//                // This should always be `mapbox` (not your username).
//                username = "mapbox"
//                // Use the secret token you stored in gradle.properties as the password
//                password = MAPBOX_DOWNLOADS_TOKEN
//            }
        }
    }
}

rootProject.name = "CMU_09_8220169_8220307_8220337"
include(":app")
 