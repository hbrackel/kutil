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

import de.macnix.util.manifest.ManifestHelper
import spock.lang.Specification

import java.util.jar.Manifest

class BuildInfoSpec extends Specification{
    static String defaultPropertiesFilename = BuildInfo.DEFAULT_BUILDINFO_FILENAME
    static String anotherPropertiesFilename = '/buildinfo2.properties'

    def "create BuildInfo from default properties file (no argument constructor)"() {
        when:
        def buildInfo = BuildInfo.fromBuildInfoProperties()

        then:
        buildInfo != null

    }
    def "create BuildInfo from custom properties file (filename constructor)"() {
        when:
        def buildInfo = BuildInfo.fromBuildInfoProperties(anotherPropertiesFilename)

        then:
        buildInfo != null
    }

    def "create BuildInfo from properties file (File constructor)"() {
        given:
        File file = new File(BuildInfoSpec.class.getResource(defaultPropertiesFilename).toURI())

        when:
        def buildInfo = BuildInfo.fromBuildInfoProperties(file)

        then:
        buildInfo != null
    }
    def "create BuildInfo from properties inputStream (InputStream constructor)"() {
        given:
        def fileName = '/buildinfo2.properties'
        InputStream inputStream= BuildInfoSpec.class.getResourceAsStream(defaultPropertiesFilename)

        when:
        def buildInfo = BuildInfo.fromBuildInfoProperties(inputStream)

        then:
        buildInfo != null
    }

    def "create BuildInfo from Manifest"() {
        given:
        Manifest manifest = ManifestHelper.getManifest(Specification).get()

        when:
        def buildInfo = BuildInfo.fromManifest(manifest)

        then:
        buildInfo != null

    }

}
