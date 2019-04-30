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

package de.macnix.util.buildinfo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*


@DisplayName("BuildInfo")
class BuildInfoTests {

    private fun assertEqualsStandardBuildInfoProperties(buildInfo: BuildInfo, properties: Properties) {
        assertThat(buildInfo.name).isEqualTo(properties["name"])
        assertThat(buildInfo.version).isEqualTo(properties["version"])
        assertThat(buildInfo.buildNumber).isEqualTo(properties["buildNumber"])
        assertThat(buildInfo.applicationName).isEqualTo(properties["applicationName"])
        assertThat(buildInfo.buildDate).isEqualTo(properties["buildDate"])
        assertThat(buildInfo.vendor).isEqualTo(properties["vendor"])
    }

    private fun assertEqualsExtraProperties(buildInfo: BuildInfo, properties: Properties) {
        properties.filter { (key, _) -> !BuildInfo.propertyNames.contains(key) }
                .forEach { (key, _) ->
                    assertThat(buildInfo.extraProperties[key]).isEqualTo(properties[key])
                }
    }

    @DisplayName("fromPath")
    @Nested
    inner class FromPath {

        @Nested
        inner class ExistingPropertiesFileInDefaultLocation {
            private val locationPath = BuildInfo.DEFAULT_BUILDINFO_FILENPATH
            private val buildInfoFileProperties = Properties().apply { load(BuildInfoTests::class.java.getResourceAsStream(locationPath)) }
            private val buildInfo = BuildInfo.fromPath()

            @Test
            fun `it should return a BuildInfo instance when called with no arguments`() {
                assertThat(buildInfo).isInstanceOf(BuildInfo::class.java)
            }

            @Test
            fun `it should contain all standard BuildInfo properties`() {
                assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
            }

            @Test
            fun `it should have non-standard parameters in the 'extraProperties'`() {
                assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
            }
        }

        @Nested
        inner class ExistingPropertiesFileInNonStandardFilePath {
            private val locationPath = "/morePropertyFiles/non-defaultFilePath.properties"
            val buildInfoFileProperties = Properties().apply { load(BuildInfoTests::class.java.getResourceAsStream(locationPath)) }
            val buildInfo = BuildInfo.fromPath(locationPath)

            @Test
            fun `it should return a BuildInfo instance when called with the path of the properties file`() {
                assertThat(buildInfo).isInstanceOf(BuildInfo::class.java)
            }

            @Test
            fun `it should contain all standard BuildInfo properties`() {
                assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
            }

            @Test
            fun `it should have non-standard parameters in the 'extraProperties'`() {
                assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
            }

        }
    }

    @DisplayName("fromFile")
    @Nested
    inner class FromFile {

    }

    @DisplayName("fromStream")
    @Nested
    inner class FromStream {

    }

    @DisplayName("fromManifest")
    @Nested
    inner class FromManifest {

    }


//            on("BuildInfo.fromFile(<file>) ") {
//                val buildInfoFile = File(BuildInfo::class.java.getResource(locationPath).toURI())
//
//                val buildInfo = BuildInfo.fromFile(buildInfoFile)
//
//                it("should return a BuildInfo instance") {
//                    assertThat(buildInfo).isNotNull
//                }
//
//                it("should contain all standard BuildInfo properties") {
//                    assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
//                }
//
//                it("should have non-standard parameters in the 'extraProperties'") {
//                    assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
//                }
//            }
//
//            on("BuildInfo.fromStream(<inputStream>) ") {
//                val buildInfoStream = BuildInfo::class.java.getResourceAsStream(locationPath)
//
//                val buildInfo = BuildInfo.fromStream(buildInfoStream)
//
//                it("should return a BuildInfo instance") {
//                    assertThat(buildInfo).isNotNull()
//                }
//
//                it("should contain all standard BuildInfo properties") {
//                    assertEqualsStandardBuildInfoProperties(buildInfo, buildInfoFileProperties)
//                }
//
//                it("should have non-standard parameters in the 'extraProperties'") {
//                    assertEqualsExtraProperties(buildInfo, buildInfoFileProperties)
//                }
//            }
//
//
//        }
//
//        given("a manifest exists with buildInfo attributes") {
//            val goodManifest = Manifest(BuildInfoTests::class.java.getResourceAsStream("/GoodManifest.MF"))
//
//            on("BuildInfo.fromManifest(<manifest>)") {
//                val buildInfo = BuildInfo.fromManifest(goodManifest)
//
//                it("should return a BuildInfo instance") {
//                    assertThat(buildInfo).isNotNull
//                }
//                it("should contain all standard BuildInfo properties") {
//                    assertThat(buildInfo.name).isEqualTo("test-library")
//                    assertThat(buildInfo.version).isEqualTo("1.3")
//                    assertThat(buildInfo.buildNumber).isEqualTo("123")
//                    assertThat(buildInfo.applicationName).isEqualTo("My Application")
//                    assertThat(buildInfo.buildDate).isEqualTo("2017-11-15 19:14:34")
//                    assertThat(buildInfo.vendor).isEqualTo("macnix.de")
//                }
//
//                it("should capture all non-standard properties in the 'extraProperties'") {
//                    val ep = buildInfo.extraProperties
//
//                    assertThat(buildInfo.extraProperties.size).isEqualTo(3)
//                    assertThat(ep["Manifest-Version"]).isEqualTo("1.0")
//                    assertThat(ep["Created-By"]).isEqualTo("Hans-Uwe Brackel")
//                    assertThat(ep["Built-By"]).isEqualTo("the builder")
//                }
//            }
//        }
//
//        given("a buildinfo with version and buildNumber") {
//            val version = "5.6.7"
//            val buildNo = "321"
//            val buildInfo = BuildInfo("myName", "appName", version, buildNo, null, null, emptyMap())
//
//            on("getVersionWithBuild()") {
//                val versionWithBuild = buildInfo.getVersionWithBuild()
//
//                it("should return a string contatenation of the version and the buildNumber") {
//                    assertThat(versionWithBuild).isEqualTo("$version-$buildNo")
//                }
//            }
//        }
//
//        given("a buildinfo with version and no buildNumber") {
//            val version = "5.6.7"
//            val buildNo = null
//            val buildInfo = BuildInfo("myName", "appName", version, buildNo, null, null, emptyMap())
//
//            on("getVersionWithBuild()") {
//                val versionWithBuild = buildInfo.getVersionWithBuild()
//
//                it("should return the version string") {
//                    assertThat(versionWithBuild).isEqualTo(version)
//                }
//            }
//        }
//
//        given("a buildinfo with a buildDate string in standard format") {
//            val buildDateString = "2017-11-12T12:13:14"
//            val buildInfo = BuildInfo("myName", null, "1.2.3", null, buildDateString, null, emptyMap())
//
//            on("getBuildDateAsLocalDateTime()") {
//                val buildDate = buildInfo.getBuildDateAsLocalDateTime()
//                val expectedDateTime = LocalDateTime.of(2017, Month.NOVEMBER, 12, 12, 13, 14)
//
//                it("should return the correct date instance") {
//                    assertThat(buildDate).isEqualTo(expectedDateTime)
//                }
//            }
//        }
//
//        given("a buildinfo with no buildDate") {
//            val buildInfo = BuildInfo("myName", null, "1.2.3", null, null, null, emptyMap())
//
//            on("getBuildDateAsLocalDateTime()") {
//                val buildDate = buildInfo.getBuildDateAsLocalDateTime()
//
//                it("should return null") {
//                    assertThat(buildDate).isNull()
//                }
//            }
//        }
//
//        given("a buildinfo with a buildDate string in standard format and an invalid format pattern") {
//            val buildDateString = "2017-11-12T12:13:14"
//            val buildInfo = BuildInfo("myName", null, "1.2.3", null, buildDateString, null, emptyMap())
//
//            on("getBuildDateAsLocalDateTime(<invalid-format-pattern>)") {
//                val exception = try {
//                    buildInfo.getBuildDateAsLocalDateTime(DateTimeFormatter.ofPattern("abc-xxx1"))
//                } catch (e: Exception) {
//                    e
//                }
//                it("should throw an IllegalArgumentException") {
//                    assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
//                }
//            }
//        }
//
//        given("a buildinfo with a buildDate string in standard format and not matching format pattern") {
//            val buildDateString = "2017-11-16T12:13:14"
//            val buildInfo = BuildInfo("myName", null, "1.2.3", null, buildDateString, null, emptyMap())
//
//            on("getBuildDateAsLocalDateTime(<not-matching-pattern>)") {
//                val exception = try {
//                    buildInfo.getBuildDateAsLocalDateTime(DateTimeFormatter.ofPattern("yyyy-dd-mm"))
//                } catch (e: Exception) {
//                    e
//                }
//                it("should throw an DateTimeParseException") {
//                    assertThat(exception).isInstanceOf(DateTimeParseException::class.java)
//                }
//            }
//        }
//
//        given("a buildinfo with an invalid buildDate string") {
//            val buildDateString = "abc 123"
//            val buildInfo = BuildInfo("myName", null, "1.2.3", null, buildDateString, null, emptyMap())
//
//            on("getBuildDateAsLocalDateTime()") {
//                val exception = try {
//                    buildInfo.getBuildDateAsLocalDateTime()
//                } catch (e: Exception) {
//                    e
//                }
//                it("should throw an DateTimeParseException") {
//                    assertThat(exception).isInstanceOf(DateTimeParseException::class.java)
//                }
//            }
//        }
//
//        context("BuildInfo validation") {
//            given("a properties file does not contain a 'name' property") {
//                val propertyFilePath = "/morePropertyFiles/missingBuildInfoName.properties"
//
//                on("BuildInfo.fromPath(<path>)") {
//                    val exception: Exception? = try {
//                        BuildInfo.fromPath(propertyFilePath)
//                        null
//                    } catch (e: Exception) {
//                        e
//                    }
//                    it("should throw a BuildInfoException") {
//                        assertThat(exception).isInstanceOf(BuildInfo.BuildInfoException::class.java)
//                        assertThat(exception?.message).startsWith("Properties do not contain values for mandatory keys 'name' or 'version'")
//                    }
//                }
//            }
//
//            given("a properties file does not contain a 'version' property") {
//                val propertyFilePath = "/morePropertyFiles/missingBuildInfoVersion.properties"
//
//                on("BuildInfo.fromPath(<path>)") {
//                    val exception: Exception? = try {
//                        BuildInfo.fromPath(propertyFilePath)
//                        null
//                    } catch (e: Exception) {
//                        e
//                    }
//                    it("should throw a BuildInfoException") {
//                        assertThat(exception).isInstanceOf(BuildInfo.BuildInfoException::class.java)
//                        assertThat(exception?.message).startsWith("Properties do not contain values for mandatory keys 'name' or 'version'")
//                    }
//                }
//            }
//
//            given("a properties file does not exist at the filePath") {
//                val propertiesFilePath = "/morePropertyFiles/someNonExistingBuildInfo.properties"
//
//                on("BuildInfo.fromPath(<path>)") {
//                    val exception: Exception? = try {
//                        BuildInfo.fromPath(propertiesFilePath)
//                        null
//                    } catch (e: Exception) {
//                        e
//                    }
//                    it("should throw a BuildInfoException") {
//                        assertThat(exception).isInstanceOf(BuildInfo.BuildInfoException::class.java)
//                        assertThat(exception).hasMessage("Properties file '$propertiesFilePath' does not exist")
//                    }
//                }
//            }
//
//            given("a properties file does not exist") {
//                val propertiesFilePath = "/morePropertyFiles"
//                val file = File(BuildInfoTests::class.java.getResource(propertiesFilePath).toURI().toString(), "someNonExistingBuildInfo.properties")
//
//                on("BuildInfo.fromFile(<file>)") {
//                    val exception: Exception? = try {
//                        BuildInfo.fromFile(file)
//                        null
//                    } catch (e: Exception) {
//                        e
//                    }
//                    it("should throw a BuildInfoException") {
//                        assertThat(exception).isInstanceOf(BuildInfo.BuildInfoException::class.java)
//                        assertThat(exception).hasMessage("Properties file '$file' does not exist")
//                    }
//                }
//            }
//
//            given("a BuildInfo instance") {
//                val buildInfo = BuildInfo("TheName", "TheApplicationName", "1.2.3", "93", "2017-11-23", "TheVendor",
//                        mapOf((Pair("anExtraProperty", "TheExtraValue"))))
//                on("toProperties()") {
//                    val buildInfoProperties = buildInfo.toProperties()
//
//                    it("should return a Properties instance") {
//                        assertThat(buildInfoProperties).isInstanceOf(Properties::class.java)
//                    }
//                    it("should contain all buildInfo attributes in the properties") {
//                        assertThat(buildInfoProperties.getProperty("name")).isEqualTo("TheName")
//                        assertThat(buildInfoProperties.getProperty("applicationName")).isEqualTo("TheApplicationName")
//                        assertThat(buildInfoProperties.getProperty("version")).isEqualTo("1.2.3")
//                        assertThat(buildInfoProperties.getProperty("buildNumber")).isEqualTo("93")
//                        assertThat(buildInfoProperties.getProperty("buildDate")).isEqualTo("2017-11-23")
//                        assertThat(buildInfoProperties.getProperty("vendor")).isEqualTo("TheVendor")
//                        assertThat(buildInfoProperties.getProperty("anExtraProperty")).isEqualTo("TheExtraValue")
//                        assertThat(buildInfoProperties.keys.size).isEqualTo(7)
//                    }
//                }
//            }
//
}
