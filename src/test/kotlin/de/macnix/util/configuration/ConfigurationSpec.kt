/*
 * Copyright (c) 2017 Hans-Uwe Brackel
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

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.File
import java.nio.file.Files


object ConfigurationSpec : Spek({
    val configurationFileName = "build/testConfiguration/application_configuration.json"
    val configurationFile = File(configurationFileName)
    val nonExistingConfigurationFile = File("build/testConfiguration/non_existing_application_configuration.json")

    fun deleteTestFiles() {
        Files.deleteIfExists(configurationFile.toPath())
        Files.deleteIfExists(File(configurationFile.parent).toPath())
    }

    describe("a Configuration") {
        beforeEachTest { deleteTestFiles() }
        afterEachTest { deleteTestFiles() }

        given("an application configuration with no existing config file") {

            given("a default configurationFile exists in the default resource location") {
                val appConfig = Configuration(configurationFile, ApplicationConfiguration::class.java)

                on("loadConfiguration()") {
                    val configuration = appConfig.loadConfiguration()

                    it("should read the default configuration from the jar resources path") {
                        assertThat(configuration, notNullValue())
                        assertThat(configuration?.numberOfParameters, equalTo(2))
                        assertThat(configuration?.theStringParameter, equalTo("I'm a String parameter"))
                    }
                    it("should save a copy of the default configuration file in the configured path") {
                        assertThat(configurationFile.exists(), equalTo(true))
                    }
                }
            }

            given("a default configurationFile does not exist in the default resource location") {
                val appConfig = Configuration(configurationFile = nonExistingConfigurationFile, configurationClass = ApplicationConfiguration::class.java)

                on("loadConfiguration()") {
                    val exception = try {
                        appConfig.loadConfiguration()
                        null
                    } catch (e: Exception) {
                        e
                    }
                    it("should throw a ConfigurationException") {
                        assertThat(exception, instanceOf(ConfigurationException::class.java))
                        assertThat(exception?.message, equalTo("Default configuration file ${appConfig.defaultConfigurationFilename} not found."))
                    }
                }
            }
        }
    }
})
