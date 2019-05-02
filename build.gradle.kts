plugins {
    id("kotlin-conventions")
    id("ci-publishing-conventions")
    id("de.macnix.gitversion") version "1.0.3"
}

repositories {
    maven { url = upstreamRepoUrl() }
    maven { url = libsReleaseRepoUrl() }
}

group = "de.macnix.util"

dependencies {
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
}