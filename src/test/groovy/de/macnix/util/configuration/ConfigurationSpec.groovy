/*
 * Copyright (c) 2016 Hans-Uwe Brackel
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.macnix.util.configuration

import spock.lang.Specification

import java.nio.file.Files

class ConfigurationSpec extends Specification {
    static String configurationFileName = "build/testConfiguration/application_configuration.json"
    File configurationFile = new File(configurationFileName)

    File nonExistingConfigurationFile = new File("build/testConfiguration/non_existing_application_configuration.json")

    def setup() {
        deleteTestFiles()
    }

    def cleanup() {
        deleteTestFiles()
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

    def "throw ConfigurationException if default config file in jar does not exist"() {
        when:
        Configuration<ApplicationConfiguration> appConfig = new Configuration<>(nonExistingConfigurationFile, ApplicationConfiguration.class)
        ApplicationConfiguration applicationConfiguration = appConfig.loadConfiguration()

        then:
        ConfigurationException ce = thrown(ConfigurationException)
        ce.message == 'Default configuration file ' + appConfig.defaultConfigurationFilename + ' not found.'
        applicationConfiguration == null
        !nonExistingConfigurationFile.exists()
    }

    def deleteTestFiles() {
        Files.deleteIfExists(configurationFile.toPath())
        Files.deleteIfExists(new File(configurationFile.parent).toPath())
    }
}
