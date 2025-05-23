plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    alias(libs.plugins.gitversionproperties)
}


allprojects {
    group = "de.macnix.util"
    version = rootProject.version

    apply<JavaPlugin>()
    apply<MavenPublishPlugin>()

    publishing {
        repositories {
            maven {
                name = "libsReleaseLocal"
                url = uri(File(rootProject.projectDir, "libsReleaseLocal"))
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

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

dependencies {
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    api(libs.arrow.core)
    implementation(libs.slf4j.api)
    implementation(libs.jackson.databind)
    implementation(libs.bundles.vertx.kotlin)
    implementation(libs.bundles.vertx.config)

    testImplementation(libs.jackson.module.kotlin)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.awaitility.kotlin)

}

