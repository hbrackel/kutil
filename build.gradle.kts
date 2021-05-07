plugins {
    id("kotlin-conventions")
    id("ci-publishing-conventions")
    id("de.macnix.gitversion") version "2.0.0"
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
    implementation(libs.reactor.core)
    implementation(libs.bundles.vertx)
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
}
