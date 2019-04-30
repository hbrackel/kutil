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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import java.util.jar.Attributes
import java.util.jar.Manifest

data class BuildInfo(
        val name: String,
        val applicationName: String?,
        val version: String,
        val buildNumber: String?,
        val buildDate: String?,
        val vendor: String?,
        val extraProperties: Map<String, String>) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(BuildInfo::class.java)

        val DEFAULT_BUILDINFO_FILENPATH = "/buildinfo.properties"
        val DEFAULT_DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        val propertyNames = arrayOf("version", "buildNumber", "buildDate", "vendor", "name", "applicationName")

        private fun fromMap(properties: Map<String, String>): BuildInfo {
            val name = properties["name"]
            val version = properties["version"]
            if (name == null || version == null) {
                log.error("Properties do not contain values for mandatory keys 'name' or 'version' ($properties)")
                throw BuildInfoException("Properties do not contain values for mandatory keys 'name' or 'version'")
            }
            val extraProperties = properties.filter { (key, _) -> !propertyNames.contains(key) }
            return (BuildInfo(name, properties["applicationName"], version, properties["buildNumber"], properties["buildDate"], properties["vendor"], extraProperties))
        }

        @JvmStatic
        fun fromPath(propertiesFilePath: String = DEFAULT_BUILDINFO_FILENPATH): BuildInfo {
            val propertiesFileStream = BuildInfo::class.java.getResourceAsStream((propertiesFilePath))
            if (propertiesFileStream == null) {
                log.error("Properties file '$propertiesFilePath' does not exist")
                throw BuildInfoException("Properties file '$propertiesFilePath' does not exist")
            }
            val props = Properties()
            props.load(propertiesFileStream)
            return fromProperties(props)
        }

        @JvmStatic
        fun fromProperties(properties: Properties): BuildInfo {
            return fromMap(properties.map { (key, value) -> Pair(key.toString(), value.toString()) }.toMap())
        }

        @JvmStatic
        fun fromFile(propertiesFile: File): BuildInfo {
            if (!propertiesFile.exists()) {
                log.error("Properties file '$propertiesFile' does not exist")
                throw BuildInfoException("Properties file '$propertiesFile' does not exist")
            }
            val props = Properties()
            props.load(propertiesFile.inputStream())
            return fromProperties(props)
        }

        @JvmStatic
        fun fromStream(inputStream: InputStream): BuildInfo {
            val props = Properties()
            props.load(inputStream)
            return fromProperties(props)
        }

        @JvmStatic
        fun fromManifest(manifest: Manifest): BuildInfo {
            val map = manifest.mainAttributes
                    .filter { (_, value) -> value != null }
                    .map { (key, value) ->
                        when (key.toString()) {
                            Attributes.Name.SPECIFICATION_TITLE.toString() -> Pair("applicationName", value.toString())
                            Attributes.Name.IMPLEMENTATION_TITLE.toString() -> Pair("name", value.toString())
                            Attributes.Name.IMPLEMENTATION_VERSION.toString() -> Pair("version", value.toString())
                            "Implementation-Build-Date" -> Pair("buildDate", value.toString())
                            "Implementation-Build-Number" -> Pair("buildNumber", value.toString())
                            Attributes.Name.IMPLEMENTATION_VENDOR.toString() -> Pair("vendor", value.toString())
                            else -> Pair<String, String>(key.toString(), value.toString())
                        }
                    }.toMap()
            return fromMap(map)
        }
    }

    class BuildInfoException(message: String) : Exception(message)

    fun getVersionWithBuild(): String? {
        val v = version
        if (buildNumber == null) {
            return v
        } else {
            return "$v-$buildNumber"
        }
    }

    fun getBuildDateAsLocalDateTime(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME): LocalDateTime? {
        if (buildDate == null) return null
        return try {
            LocalDateTime.parse(this.buildDate, formatter)
        } catch (iae: IllegalArgumentException) {
            log.error("Invalid DateFormat pattern ({})", iae.message)
            throw iae
        } catch (dtpe: DateTimeParseException) {
            log.error("BuildDate string '{}' could not be parsed with formatter '{}'", buildDate, formatter)
            throw dtpe
        }
    }

    fun getBuildDateAsLocalDateTimeOrElse(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME, defaultLocalDateTime: LocalDateTime): LocalDateTime {
        val ldt = getBuildDateAsLocalDateTime(formatter)
        return if (ldt != null) ldt else defaultLocalDateTime
    }

    fun toProperties(): Properties {
        val properties = Properties()
        properties.setProperty("name", name)
        if (applicationName != null) properties.set("applicationName", applicationName)
        properties.setProperty("version", version)
        if (buildNumber != null) properties.setProperty("buildNumber", buildNumber)
        if (buildDate != null) properties.setProperty("buildDate", buildDate)
        if (vendor != null) properties.setProperty("vendor", vendor)
        properties.putAll(extraProperties)
        return properties
    }


}
