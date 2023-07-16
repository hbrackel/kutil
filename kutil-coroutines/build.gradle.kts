plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(libs.arrow.core)
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.arrow)
}

tasks.test {
    useJUnitPlatform()
}
