plugins {
    `java-library`
}

repositories {
    //    maven { url = upstreamRepoUrl() }
    maven { url = libsReleaseRepoUrl() }
}

dependencies {
    testImplementation("com.jayway.awaitility:awaitility-java8:${GlobalVersions.awaitilityVersion}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${GlobalVersions.junitJupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${GlobalVersions.junitJupiterVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${GlobalVersions.junitJupiterVersion}")

    testImplementation("io.mockk:mockk:${GlobalVersions.mockkVersion}")
    testImplementation("org.assertj:assertj-core:${GlobalVersions.assertjVersion}")

    testImplementation("org.apache.logging.log4j:log4j-api:${GlobalVersions.log4jVersion}")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core:${GlobalVersions.log4jVersion}")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:${GlobalVersions.log4jVersion}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    test {
        useJUnitPlatform {
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
}


