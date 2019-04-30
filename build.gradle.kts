plugins {
    id("java-library")  // needed to make dependencies 'implementation' etc be resolved
    id("org.jetbrains.kotlin.jvm") version "1.3.31"
    id("org.sonarqube") version "2.7"
    id("maven-publish")
    id("de.macnix.gitversion") version "1.0.3"
    id("de.macnix.versionfile")
}

apply(from = "$rootDir/testEnvironment.gradle")

repositories {
    maven { url = upstreamRepoUrl() }
    maven { url = libsReleaseRepoUrl() }
}

group = "de.macnix.util"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.slf4j:slf4j-api:${Deps.slf4jApiVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Deps.jacksonVersion}")
    implementation("io.projectreactor:reactor-core:${Deps.reactorCoreVersion}")
}

publishing {
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
    repositories {
        maven {
            name = "external"
            url = libsReleaseLocalRepoUrl()
            credentials {
                username = deployUser()
                password = deployPassword()
            }
        }
        maven {
            name = "downstream"
            url = downstreamRepoUrl()
        }
    }
}

tasks {
    compileKotlin {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()

        kotlinOptions {
            jvmTarget = "1.8"
            allWarningsAsErrors = true
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }

    }
}
