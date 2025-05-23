plugins {
    kotlin("jvm")
}

dependencies {
    api(libs.arrow.core)
    implementation(libs.kotlin.coroutines)
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.assertions.core)
}

tasks.test {
    useJUnitPlatform()
}
