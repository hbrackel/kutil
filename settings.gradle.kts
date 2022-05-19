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
        create("libs") { from("de.macnix:version-catalogs-maven:2022.05.19-1") }
        create("testLibs") { from("de.macnix:version-catalogs-maven-test:2022.03.20-1") }
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

