plugins {
    kotlin("jvm")
    alias(macnix.plugins.gitver)
//    id("ci-publishing-conventions")
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
    testImplementation(testLibs.junit.jupiter)
    testImplementation(testLibs.assertj.core)
    testImplementation(testLibs.awaitility.kotlin)

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName("clean").dependsOn("listEnvironmentVariables")
tasks.create("listEnvironmentVariables") {
    System.getenv().forEach {
        println("ENV: ${it.key}: ${it.value}")
    }

}
publishing {
    repositories {
        maven {
            name = "external"
            url = libsReleaseLocalRepoUrl()
            isAllowInsecureProtocol = true
            credentials {
                username = deployUser()
                password = deployPassword()
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
