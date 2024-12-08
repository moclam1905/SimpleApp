pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // fetch dagger plugin only
        mavenCentral() {
            content {
                includeGroup("com.google.dagger")
                includeGroup("com.google.dagger.hilt.android")
            }
            mavenContent {
                releasesOnly()
            }
        }

        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SimpleApp"
include(":app")
include(":simpleapp-data")
include(":simpleapp-database")
include(":simpleapp-model")
include(":simpleapp-test")
