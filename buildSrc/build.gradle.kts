plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()   // required to resolve embedded-kotlin compiler; see https://github.com/gradle/kotlin-dsl/issues/1033
}
