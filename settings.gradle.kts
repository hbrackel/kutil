includeBuild("../gradle-build-scripts")

pluginManagement {
    repositories {
        maven {
            url = uri("${System.getenv("REPO_PLUGINS_RELEASE") ?: extra["pluginsReleaseRepoDefaultUrl"] ?: ""}")
        }
//        gradlePluginPortal()
    }
}

gradle.rootProject {
    buildscript {
        dependencies {
            classpath("buildscripts:gradle-build-scripts:0.0.1")
        }
    }
}
rootProject.name = "kutil"

