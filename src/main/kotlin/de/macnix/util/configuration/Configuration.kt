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

import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files

class Configuration<T>(val configurationFile: File, val configurationClass: Class<T>) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(Configuration::class.java)
    }

    var configuration: T? = null
    val defaultConfigurationFilename = "/configuration/" + configurationFile.name

    @Throws(IOException::class, JsonParseException::class, JsonMappingException::class)
    private fun readConfiguration(configInputStream: InputStream): T {
        log.debug("#readConfiguration(InputStream={})", configInputStream)
        val mapper = ObjectMapper()
        try {
            return mapper.readValue(configInputStream, configurationClass)
        } catch (e: Exception) {
            log.error("#readConfiguration(): Failed to read configuration from stream {}({})", configInputStream, e.message)
            throw e
        }
    }

    @Throws(IOException::class, JsonGenerationException::class, JsonMappingException::class)
    fun writeConfiguration(configurationFile: File = this.configurationFile) {
        log.debug("#writeConfiguration(): Writing configuration file {}.", configurationFile.canonicalPath)
        val mapper = ObjectMapper()
        mapper.writeValue(configurationFile, configuration)
    }

    @Throws(IOException::class, JsonGenerationException::class, JsonParseException::class, JsonMappingException::class)
    fun loadConfiguration(): T? {
        log.info("#loadConfiguration()")

        if (!configurationFile.exists()) {
            log.info("#loadConfiguration(): Configuration file {} not found.", configurationFile.canonicalPath)
            try {
                Files.createDirectories(File(configurationFile.parent).toPath())
                val ios = Configuration::class.java.getResourceAsStream(defaultConfigurationFilename)
                if (ios == null) {
                    val errMsg = "Default configuration file $defaultConfigurationFilename not found."
                    log.error(errMsg)
                    throw ConfigurationException(errMsg)
                }
                this.configuration = readConfiguration(ios)
                writeConfiguration()
            } catch (e: IOException) {
                log.info("#loadConfiguration(): Cannot create configuration file {} ({}).", configurationFile.getCanonicalPath(), e.message)
                try {
                    Files.deleteIfExists(configurationFile.toPath())
                } catch (ioe: IOException) {
                    log.warn("#loadConfiguration(): Cannot delete configuration file {}. ({})", configurationFile.canonicalPath, ioe.message)
                }
                throw e
            }
        } else {
            log.info("#loadConfiguration(): Reading configuration from file {}.", configurationFile.getCanonicalPath())
            this.configuration = readConfiguration(configurationFile.inputStream())
        }
        return configuration
    }
}
