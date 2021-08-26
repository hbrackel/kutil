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
        create("libs") { from("de.macnix:version-catalogs-maven:latest.release") }
        create("testLibs") { from("de.macnix:version-catalogs-maven-test:latest.release") }
        create("macnix") { from("de.macnix:version-catalogs-macnix:latest.release") }
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

