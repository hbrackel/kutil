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

package de.macnix.util.buildinfo

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import java.io.File
import java.util.*
import java.util.jar.Manifest


object BuildInfoSpec : Spek({
    val defaultPropertiesFilename = BuildInfo.DEFAULT_BUILDINFO_FILENPATH
    val anotherPropertiesFilename = "/buildinfo2.properties"

    fun assertEqualsStandardBuildInfoProperties(buildInfo: BuildInfo, properties: Properties) {
        assertThat(buildInfo.name, equalTo(properties["name"]))
        assertThat(buildInfo.version, equalTo(properties["version"]))
        assertThat(buildInfo.buildNumber, equalTo(properties["buildNumber"]))
        assertThat(buildInfo.applicationName, equalTo(properties["applicationName"]))
        assertThat(buildInfo.buildDate, equalTo(properties["buildDate"]))
        assertThat(buildInfo.vendor, equalTo(properties["vendor"]))
    }

    fun assertEqualsExtraProperties(buildInfo: BuildInfo, properties: Properties) {
        properties.filter { (key, _) -> !BuildInfo.propertyNames.contains(key) }
                .forEach { (key, _) ->
                    assertThat(buildInfo.extraProperties[key], equalTo(properties[key]))
                }
    }


    describe("BuildInfo") {

        given("a properties file exists with the default filename in the default resource location") {
            val locationPath = BuildInfo.DEFAULT_BUILDINFO_FILENPATH
            val buildInfoFileProperties = Properties().apply { load(BuildInfoSpec::class.java.getResourceAsStream(locationPath)) }

            on("BuildInfo.fromPath() with no argument") {
                val buildInfo = BuildInfo.fromPath()

                it("should return a BuildInfo instance") {
                    assertThat(buildInfo, notNullValue())
                }

                it("should contain all standard BuildInfo properties") {
                    assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
                }

                it("should have non-standard parameters in the 'extraProperties'") {
                    assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
                }
            }
        }

        given("a properties file exists under a non-default file path") {
            val locationPath = "/morePropertyFiles/non-defaultFilePath.properties"
            val buildInfoFileProperties = Properties().apply { load(BuildInfoSpec::class.java.getResourceAsStream(locationPath)) }

            on("BuildInfo.fromPath(<path>)") {
                val buildInfo = BuildInfo.fromPath(locationPath)

                it("should return a BuildInfo instance") {
                    assertThat(buildInfo, notNullValue())
                }

                it("should contain all standard BuildInfo properties") {
                    assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
                }

                it("should have non-standard parameters in the 'extraProperties'") {
                    assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
                }
            }

            on("BuildInfo.fromFile(<file>) ") {
                val buildInfoFile = File(BuildInfo::class.java.getResource(locationPath).toURI())

                val buildInfo = BuildInfo.fromFile(buildInfoFile)

                it("should return a BuildInfo instance") {
                    assertThat(buildInfo, notNullValue())
                }

                it("should contain all standard BuildInfo properties") {
                    assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
                }

                it("should have non-standard parameters in the 'extraProperties'") {
                    assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
                }
            }

            on("BuildInfo.fromStream(<inputStream>) ") {
                val buildInfoStream = BuildInfo::class.java.getResourceAsStream(locationPath)

                val buildInfo = BuildInfo.fromStream(buildInfoStream)

                it("should return a BuildInfo instance") {
                    assertThat(buildInfo, notNullValue())
                }

                it("should contain all standard BuildInfo properties") {
                    assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
                }

                it("should have non-standard parameters in the 'extraProperties'") {
                    assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
                }
            }


        }

        given("a manifest exists with buildInfo attributes") {
            val goodManifest = Manifest(BuildInfoSpec::class.java.getResourceAsStream("/GoodManifest.MF"))

            on("BuildInfo.fromManifest(<manifest>)") {
                val buildInfo = BuildInfo.fromManifest(goodManifest)

                it("should return a BuildInfo instance") {
                    assertThat(buildInfo, notNullValue())
                }
                it("should contain all standard BuildInfo properties") {
                    assertThat(buildInfo.name, equalTo("test-library"))
                    assertThat(buildInfo.version, equalTo("1.3"))
                    assertThat(buildInfo.buildNumber, equalTo("123"))
                    assertThat(buildInfo.applicationName, equalTo("My Application"))
                    assertThat(buildInfo.buildDate, equalTo("2017-11-15 19:14:34"))
                    assertThat(buildInfo.vendor, equalTo("macnix.de"))
                }

                it("should have captured all non-standard properties in the 'extraProperties") {
                    val ep = buildInfo.extraProperties

                    assertThat(buildInfo.extraProperties.size, equalTo(3))
                    assertThat(ep["Manifest-Version"], equalTo("1.0"))
                    assertThat(ep["Created-By"], equalTo("Hans-Uwe Brackel"))
                    assertThat(ep["Built-By"], equalTo("the builder"))
                }
            }
        }

        context("BuildInfo validation") {
            given("a properties file does not contain a 'name' property") {
                val propertyFilePath = "/morePropertyFiles/missingBuildInfoName.properties"

                on("BuildInfo.fromPath(<path>)") {
                    val exception: Exception? = try {
                        BuildInfo.fromPath(propertyFilePath)
                        null
                    } catch (e: Exception) {
                        e
                    }
                    it("should throw a BuildInfoException") {
                        assertThat(exception, instanceOf(BuildInfo.BuildInfoException::class.java))
                        assertThat(exception?.message, startsWith("Properties do not contain values for mandatory keys 'name' or 'version'"))
                    }
                }
            }

            given("a properties file does not contain a 'version' property") {
                val propertyFilePath = "/morePropertyFiles/missingBuildInfoVersion.properties"

                on("BuildInfo.fromPath(<path>)") {
                    val exception: Exception? = try {
                        BuildInfo.fromPath(propertyFilePath)
                        null
                    } catch (e: Exception) {
                        e
                    }
                    it("should throw a BuildInfoException") {
                        assertThat(exception, instanceOf(BuildInfo.BuildInfoException::class.java))
                        assertThat(exception?.message, startsWith("Properties do not contain values for mandatory keys 'name' or 'version'"))
                    }
                }
            }

            given("a properties file does not exist at the filePath") {
                val propertiesFilePath = "/morePropertyFiles/someNonExistingBuildInfo.properties"

                on("BuildInfo.fromPath(<path>)") {
                    val exception: Exception? = try {
                        BuildInfo.fromPath(propertiesFilePath)
                        null
                    } catch (e: Exception) {
                        e
                    }
                    it("should throw a BuildInfoException") {
                        assertThat(exception, instanceOf(BuildInfo.BuildInfoException::class.java))
                        assertThat(exception?.message, equalTo("Properties file '$propertiesFilePath' does not exist"))
                    }
                }
            }

            given("a properties file does not exist") {
                val propertiesFilePath = "/morePropertyFiles"
                val file = File(BuildInfoSpec::class.java.getResource(propertiesFilePath).toURI().toString(), "someNonExistingBuildInfo.properties")

                on("BuildInfo.fromFile(<file>)") {
                    val exception: Exception? = try {
                        BuildInfo.fromFile(file)
                        null
                    } catch (e: Exception) {
                        e
                    }
                    it("should throw a BuildInfoException") {
                        assertThat(exception, instanceOf(BuildInfo.BuildInfoException::class.java))
                        assertThat(exception?.message, equalTo("Properties file '$file' does not exist"))
                    }
                }
            }


        }
    }

})
