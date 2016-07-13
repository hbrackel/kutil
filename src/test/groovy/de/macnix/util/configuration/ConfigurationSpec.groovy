package de.macnix.util.configuration

import spock.lang.Specification

import java.nio.file.Files

class ConfigurationSpec extends Specification {
    static String configurationFileName = "build/test/application_configuration.json"
    File configurationFile = new File(configurationFileName)

    def setup() {
        Files.deleteIfExists(configurationFile.toPath())
        Files.deleteIfExists(new File(configurationFile.parent).toPath())
    }

    def cleanup() {
        Files.deleteIfExists(configurationFile.toPath())
        Files.deleteIfExists(new File(configurationFile.parent).toPath())
    }

    def "read ApplicationConfiguration from default configuration in jar.resources if config file does not exist"() {
        when:
        Configuration<ApplicationConfiguration> appConfig = new Configuration<>(configurationFile, ApplicationConfiguration.class)
         ApplicationConfiguration applicationConfiguration = appConfig.loadConfiguration()

        then:
        applicationConfiguration != null
        configurationFile.exists()
        applicationConfiguration.numberOfParameters == 2
        applicationConfiguration.theStringParameter == "I'm a String parameter"
    }

}
