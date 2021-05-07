plugins {
    id("maven-publish")
    id("versionfile")
}

publishing {
    repositories {
        maven {
            name = "external"
            url = libsReleaseLocalRepoUrl()
            isAllowInsecureProtocol = true
            credentials {
                username = deployUser()
                password = deployPassword()
            }
        }
        maven {
            name = "downstream"
            url = downstreamRepoUrl()
            isAllowInsecureProtocol = true
        }
    }
}
