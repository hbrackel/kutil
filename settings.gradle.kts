pluginManagement {
    repositories {
        maven {
            url = uri("${System.getenv("REPO_PLUGINS_RELEASE") ?: extra["pluginsReleaseRepoDefaultUrl"] ?: ""}")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("${System.getenv("REPO_LIBS_RELEASE") ?: extra["libsReleaseRepoDefaultUrl"] ?: ""}")
            isAllowInsecureProtocol = true
        }
    }
}

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("testLibs") { from(files("gradle/versions/test-libs.versions.toml")) }
        create("libs") { from(files("gradle/versions/libs.versions.toml")) }
    }

    repositories {
        mavenCentral()
        maven {
            url = uri("${System.getenv("REPO_LIBS_RELEASE") ?: extra["libsReleaseRepoDefaultUrl"] ?: ""}")
            isAllowInsecureProtocol = true
        }
    }
}

rootProject.name = "kutil"

