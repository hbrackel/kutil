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

package de.macnix.util.manifest

import spock.lang.Specification

import java.util.jar.Manifest

class ManifestHelperSpec extends Specification {

    def "get Manifest of class from jar"() {
        given:
        def clazz = Specification

        when:
        Optional<Manifest> manifest = ManifestHelper.getManifest(clazz)

        then:
        manifest.present
    }

    def "get Manifest of class which is not in a jar"() {
        given:
        def clazz = MyTestClass

        when:
        Optional<Manifest> manifest = ManifestHelper.getManifest(clazz)

        then:
        !manifest.present
    }

    def "read an existing attribute from Manifest"() {
        given:
        def clazz = Specification
        def attributeName = "Bundle-Name"
        def defaultValue = "defaultValue"

        when:
        Optional<Manifest> manifest = ManifestHelper.getManifest(clazz)
        String attrValue = ManifestHelper.getOrDefault(manifest.get(), attributeName, defaultValue)

        then:
        manifest.get().mainAttributes.each {key, value -> println("$key -> $value")}
        attrValue != null
        attrValue != defaultValue
    }

    def "try reading an non-existing attribute from Manifest"() {
        given:
        def clazz = Specification
        def defaultValue = "defaultValue"
        def attributeName = "I'm not exisiting"

        when:
        Optional<Manifest> manifest = ManifestHelper.getManifest(clazz)
        String usesValue = ManifestHelper.getOrDefault(manifest.get(), attributeName, defaultValue)

        then:
        notThrown(IllegalArgumentException)
        usesValue == defaultValue
    }

    class MyTestClass {}
}
