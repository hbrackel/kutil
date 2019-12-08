val publishVersionFile by tasks.registering {
    group = "publishing"
    description = "Writes the version of the published artifact to a text file"
    doLast {
        with(file("$buildDir/libs/${project.name.toLowerCase()}-version.txt")) {
            parentFile.mkdirs()
            writeText("${project.version}")
        }
    }
}

tasks.getByName("assemble").dependsOn(publishVersionFile)
