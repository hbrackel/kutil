import org.gradle.api.Project
import java.net.URI

fun Project.downstreamRepoUrl(): URI = URI("file://$buildDir/repo")
fun Project.upstreamRepoUrl(): URI = URI("file://$rootDir/upstream-repo")

fun Project.libsReleaseRepoUrl(): URI = URI(System.getenv("REPO_LIBS_RELEASE")
        ?: findProperty("libsReleaseRepoDefaultUrl") as String)

fun Project.libsReleaseLocalRepoUrl(): URI = URI(System.getenv("REPO_LIBS_RELEASE_LOCAL")
        ?: findProperty("libsReleaseLocalRepoDefaultUrl") as String)

fun Project.pluginsReleaseRepoUrl(): URI = URI(System.getenv("REPO_PLUGINS_RELEASE")
        ?: findProperty("pluginsReleaseRepoDefaultUrl") as String)

fun Project.pluginsReleaseLocalRepoUrl(): URI = URI(System.getenv("REPO_PLUGINS_RELEASE_LOCAL")
        ?: findProperty("pluginsReleaseLocalRepoDefaultUrl") as String)

fun Project.deployUser(): String = System.getenv("MAVEN_DEPLOY_USR")
        ?: findProperty("mavenDeployUser") as String

fun Project.deployPassword(): String = System.getenv("MAVEN_DEPLOY_PSW")
        ?: findProperty("mavenDeployPassword") as String