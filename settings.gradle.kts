println("kutil: settings.gradle.kts")
pluginManagement {
    repositories {
        maven {
            url = uri("${System.getenv("REPO_PLUGINS_RELEASE") ?: extra["pluginsReleaseRepoDefaultUrl"] ?: ""}")
        }
    }
}
rootProject.name = "kutil"

