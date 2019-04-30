import java.net.URI

plugins {
    `kotlin-dsl`
}

fun Project.pluginsReleaseRepoUrl(): URI = URI(System.getenv("REPO_PLUGINS_RELEASE")
        ?: findProperty("pluginsReleaseRepoDefaultUrl") as String ?: "")

repositories {
    maven { url = pluginsReleaseRepoUrl() }
}

dependencies {
//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.31")
}
