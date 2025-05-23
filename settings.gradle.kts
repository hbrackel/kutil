dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("./gradle/versions/libs.versions.toml"))
        }
    }
    repositories {
        mavenCentral()
    }
}

rootProject.name = "kutil"
include(
    "kutil-coroutines",
)
