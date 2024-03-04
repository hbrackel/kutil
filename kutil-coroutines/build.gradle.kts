plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(project(":kutil-function"))
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.assertions.core)
}

tasks.test {
    useJUnitPlatform()
}
