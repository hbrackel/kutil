/*
 * Copyright (c) 2019 Hans-Uwe Brackel
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

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.*
import java.io.File
import java.nio.file.Files


@DisplayName("A Configuration")
class ConfigurationTests {
    private val configurationFileName = "build/testConfiguration/application_configuration.json"
    val configurationFile = File(configurationFileName)
    val nonExistingConfigurationFile = File("build/testConfiguration/non_existing_application_configuration.json")

    private fun deleteTestFiles() {
        Files.deleteIfExists(configurationFile.toPath())
        Files.deleteIfExists(File(configurationFile.parent).toPath())
    }

    @BeforeEach
    fun beforeEach() {
        deleteTestFiles()
    }

    @AfterEach
    fun afterEach() {
        deleteTestFiles()
    }

    @DisplayName("loadConfiguration")
    @Nested
    inner class LoadConfiguration {

        @DisplayName("Given an application configuration without an existing config file")
        @Nested
        inner class ApplicationWithoutConfigFile {

            @DisplayName("Given a default configuration file exists in the default resource location")
            @Nested
            inner class DefaultConfigurationFileInDefaultResourceLocation {

                private val appConfig: Configuration<ApplicationConfiguration> = Configuration(configurationFile, ApplicationConfiguration::class.java)

                @Test
                fun `it should read the default configuration from the jar resources path`() {
                    val configuration = appConfig.loadConfiguration()
                    assertThat(configuration).isNotNull
                    assertThat(configuration?.numberOfParameters).isEqualTo(2)
                    assertThat(configuration?.theStringParameter).isEqualTo("I'm a String parameter")
                }

                @Test
                fun `it should save a copy of the default configuration file in the configured path`() {
                    assertThat(configurationFile.exists()).isTrue()
                }
            }

            @DisplayName("Given a default configurationFile does not exist in the default resource location")
            @Nested
            inner class DefaultConfigurationFileNotExistsInDefaultResourceLocation {
                val appConfig = Configuration(configurationFile = nonExistingConfigurationFile, configurationClass = ApplicationConfiguration::class.java)

                @Test
                fun `it should throw a ConfigurationException`() {
                    assertThatExceptionOfType(ConfigurationException::class.java)
                            .isThrownBy({ appConfig.loadConfiguration() })
                            .withMessage("Default configuration file ${appConfig.defaultConfigurationFilename} not found.")
                }
            }
        }
    }
}
