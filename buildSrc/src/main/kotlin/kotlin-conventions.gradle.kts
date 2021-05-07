plugins {
    kotlin("jvm")
}

repositories {
    //    maven { url = upstreamRepoUrl() }
    maven {
        url = libsReleaseRepoUrl()
        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    // assume logging is required is every project
    implementation("org.slf4j:slf4j-api:${GlobalVersions.slf4jApiVersion}")


//    testImplementation("com.jayway.awaitility:awaitility:${GlobalVersions.awaitilityVersion}")
    testImplementation("org.awaitility:awaitility-kotlin:${GlobalVersions.awaitilityKotlinVersion}")

    testImplementation("org.junit.jupiter:junit-jupiter:${GlobalVersions.junitJupiterVersion}")

    testImplementation("io.mockk:mockk:${GlobalVersions.mockkVersion}")
    testImplementation("org.assertj:assertj-core:${GlobalVersions.assertjVersion}")

    testImplementation("org.apache.logging.log4j:log4j-api:${GlobalVersions.log4jVersion}")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core:${GlobalVersions.log4jVersion}")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:${GlobalVersions.log4jVersion}")

}

tasks {
    compileKotlin {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()

        kotlinOptions {
            jvmTarget = "1.8"
//            allWarningsAsErrors = true
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }

    }

    test {
        useJUnitPlatform {
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }

}


