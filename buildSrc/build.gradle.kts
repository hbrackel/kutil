import java.net.URI

plugins {
    `kotlin-dsl`
}

fun pluginsReleaseRepoUrl(): URI = URI(
        System.getenv("REPO_PLUGINS_RELEASE") ?: findProperty("pluginsReleaseRepoDefaultUrl") as String)

repositories {
    maven {
        url = pluginsReleaseRepoUrl()
        isAllowInsecureProtocol = true
    }
    gradlePluginPortal()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
}
