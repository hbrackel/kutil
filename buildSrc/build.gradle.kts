import java.net.URI

plugins {
    `kotlin-dsl`
}

fun pluginsReleaseRepoUrl(): URI = URI(
        System.getenv("REPO_PLUGINS_RELEASE") ?: findProperty("pluginsReleaseRepoDefaultUrl") as String)

repositories {
    maven { url = pluginsReleaseRepoUrl() }
    gradlePluginPortal()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
}