plugins {
    id("maven-publish")
    id("versionfile")
}

publishing {
    repositories {
        maven {
            name = "external"
            url = libsReleaseLocalRepoUrl()
            credentials {
                username = deployUser()
                password = deployPassword()
            }
        }
        maven {
            name = "downstream"
            url = downstreamRepoUrl()
        }
    }
}
