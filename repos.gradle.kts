project.extra.set("deployUser", System.getenv("MAVEN_DEPLOY_USR") ?: project.findProperty("mavenDeployUser")
?: "")  // from ~/.gradle/gradle.properties
project.extra.set("deployPassword", System.getenv("MAVEN_DEPLOY_PSW") ?: project.findProperty("mavenDeployPassword")
?: "")

project.extra.set("libsReleaseRepoUrl", System.getenv("REPO_LIBS_RELEASE")
        ?: project.findProperty("libsReleaseRepoDefaultUrl") ?: "")
project.extra.set("libsReleaseLocalRepoUrl", System.getenv("REPO_LIBS_RELEASE_LOCAL")
        ?: project.findProperty("libsReleaseLocalRepoDefaultUrl") ?: "")

project.extra.set("pluginsReleaseRepoUrl", System.getenv("REPO_PLUGINS_RELEASE")
        ?: project.findProperty("pluginsReleaseRepoDefaultUrl") ?: "")
project.extra.set("pluginsReleaseLocalRepoUrl", System.getenv("REPO_PLUGINS_RELEASE_LOCAL")
        ?: project.findProperty("pluginsReleaseLocalRepoDefaultUrl") ?: "")
project.extra.set("upstreamRepoUrl", "$rootDir/upstream-repo")
project.extra.set("downstreamRepoUrl", "$buildDir/repo")


