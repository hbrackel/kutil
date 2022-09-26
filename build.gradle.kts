import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    kotlin("jvm")
    alias(macnix.plugins.gitver)
    alias(libs.plugins.ben.manes.versions)
    id("ci-publishing-conventions")
}

repositories {
    maven {
        url = upstreamRepoUrl()
        isAllowInsecureProtocol = true
    }
    maven {
        url = libsReleaseRepoUrl()
        isAllowInsecureProtocol = true
    }
}

group = "de.macnix.util"

dependencies {
    implementation(libs.slf4j.api)
    implementation(libs.jackson.databind)
    implementation(libs.bundles.vertx.kotlin)
    implementation(libs.bundles.vertx.config)

    testImplementation(libs.jackson.module.kotlin)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.awaitility.kotlin)

}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

publishing {
    repositories {
        maven {
            name = "libsReleaseLocal"
            url =
                uri("${System.getenv("REPO_LIBS_RELEASE_LOCAL") ?: extra["libsReleaseLocalRepoDefaultUrl"] ?: ""}")
            isAllowInsecureProtocol = true

            credentials {
                username = (System.getenv("MAVEN_DEPLOY_USR") ?: extra["mavenDeployUser"]) as String
                password = (System.getenv("MAVEN_DEPLOY_PSW") ?: extra["mavenDeployPassword"]) as String
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("hbrackel")
                        name.set("Hans-Uwe Brackel")
                        email.set("hbrackel@googlemail.com")
                    }
                }
            }
        }
    }
}
