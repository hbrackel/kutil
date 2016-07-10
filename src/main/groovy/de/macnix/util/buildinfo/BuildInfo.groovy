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

package de.macnix.util.buildinfo

import groovy.transform.ToString
import groovy.util.logging.Slf4j

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.jar.Attributes
import java.util.jar.Manifest

@ToString(includeFields = true, includeNames = true, includePackage = false)
@Slf4j
class BuildInfo {
    static final String DEFAULT_BUILDINFO_FILENAME = "/buildinfo.properties"
    static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

    def version
    def buildNumber
    def buildDate
    def vendor
    def name
    def applicationName
    private extraProperties = [:]

    def getVersionWithBuild() {
        def v = version
        if (v == null) {
            if (buildNumber == null) return null
            return "build ${buildNumber}"
        } else {
            if (buildNumber == null) return v
            return "${v}-${buildNumber}"
        }
    }

    Date getBuildDate(String dateFormat = DEFAULT_DATEFORMAT) {
        if (buildDate == null) return null
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat)
            return sdf.parse(this.buildDate)
        }
        catch (ParseException ignored) {
            return null
        }
    }

    String getBuildDateAsString() {
        if (this.buildDate == null) return null
        return this.buildDate.toString()
    }

    def getExtraProperties() {
        this.extraProperties
    }

    private void propertyMissing(String name, value) {
        log.info("Unrecognized property in buildInfo: $name=$value")
        this.extraProperties[name] = value
    }


    static BuildInfo fromBuildInfoProperties(String propertiesFileName = DEFAULT_BUILDINFO_FILENAME) throws IOException, NullPointerException {
        def props = new Properties()
        props.load(BuildInfo.getClass().getResourceAsStream(propertiesFileName))
        new BuildInfo(props)
    }

    static BuildInfo fromBuildInfoProperties(File propertiesFile) throws IOException {
        def props = new Properties()
        propertiesFile.withInputStream { props.load(it) }
        new BuildInfo(props)
    }

    static BuildInfo fromBuildInfoProperties(InputStream inputStream) throws IOException {
        def props = new Properties()
        props.load(inputStream)
        new BuildInfo(props)
    }

    static BuildInfo fromManifest(Manifest manifest) throws NullPointerException {
        if (manifest == null) throw new NullPointerException("Manifest must not be null")
        def map = manifest.mainAttributes.collectEntries { key, value ->
            def newKey = key.toString()
            switch (newKey) {
                case Attributes.Name.SPECIFICATION_TITLE.toString():
                    newKey = "applicationName"
                    break
                case Attributes.Name.SPECIFICATION_VERSION.toString():
                    newKey = "version"
                    break
                case Attributes.Name.IMPLEMENTATION_TITLE.toString():
                    newKey = "name"
                    break
                case Attributes.Name.IMPLEMENTATION_VERSION.toString():
                    newKey = "buildNumber"
                    break
                case "Implementation-BuildDate":
                    newKey = "buildDate"
                    break
                case Attributes.Name.IMPLEMENTATION_VENDOR.toString():
                    newKey = "vendor"
                    break
                default:
                    break
            }
            [(newKey): (value ? value.toString() : null)]
        }
        new BuildInfo(map)
    }


}
