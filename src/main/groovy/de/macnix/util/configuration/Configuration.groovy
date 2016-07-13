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

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j

import java.nio.file.Files

@Slf4j
class Configuration<T> {
    final File configurationFile
    T configuration
    final Class<T> configurationClass

    Configuration(File configurationFile, Class<T>configurationClass) {
        this.configurationFile = configurationFile
        this.configurationClass = configurationClass
    }

    T loadConfiguration() throws IOException, URISyntaxException {
        log.info("#loadConfiguration()")
        if (!configurationFile.exists()) {
            log.info("#loadConfiguration(): Configuration file {} not found.", configurationFile.getCanonicalPath())
            try {
                Files.createDirectories(new File(configurationFile.getParent()).toPath())
                this.configuration = readConfiguration(getClass().getResourceAsStream("/configuration/" + configurationFile.getName()))
                writeConfiguration()
            } catch (IOException e) {
                log.info("#loadConfiguration(): Cannot create configuration file {} ({}).", configurationFile.getCanonicalPath(), e.getMessage())
                try {
                    Files.deleteIfExists(configurationFile.toPath())
                } catch (IOException ignored) {
                    log.warn("#loadConfiguration(): Cannot delete configuration file {}.", configurationFile.getCanonicalPath())
                }
            }
        } else {
            log.info("#loadConfiguration(): Reading configuration from file {}.", configurationFile.getCanonicalPath())
            this.configuration = readConfiguration(configurationFile.newInputStream())
        }
        configuration
    }

    private T readConfiguration(InputStream configInputStream) throws IOException {
        log.debug("#readConfiguration(InputStream={})", configInputStream)
        ObjectMapper mapper = new ObjectMapper()
        return mapper.readValue(configInputStream, configurationClass)
    }

    void writeConfiguration(File configurationFile = this.configurationFile) throws IOException {
        log.debug("#writeConfiguration(): Writing configuration file {}.", configurationFile.getCanonicalPath())
        ObjectMapper mapper = new ObjectMapper()
        mapper.writeValue(configurationFile, configuration)
    }


}
