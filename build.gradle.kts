import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    alias(macnix.plugins.gitver)
}

group = "de.macnix.util"

dependencies {
    implementation(libs.slf4j.api)
    implementation(libs.jackson.databind)
    implementation(libs.bundles.vertx.kotlin)
    implementation(libs.bundles.vertx.config)
    implementation(libs.arrow.core)

    testImplementation(libs.jackson.module.kotlin)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.awaitility.kotlin)

}
java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "11"
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
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
