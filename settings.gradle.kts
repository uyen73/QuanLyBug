pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://jcenter.bintray.com")
            maven {url = uri("https://jitpack.io")}
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://jcenter.bintray.com")
            maven {url = uri("https://jitpack.io")}
        }
    }
}

rootProject.name = "QuanLyBug"
include(":app")
