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

import java.util.jar.Attributes
import java.util.jar.Manifest

class ManifestHelper {

    /**
     * Inspired by the solution in <a href="http://stackoverflow.com/questions/3777055/reading-manifest-mf-file-from-jar-file-using-java">http://stackoverflow.com/questions/3777055/reading-manifest-mf-file-from-jar-file-using-java</a>
     */
    static Optional<Manifest> getManifest(Class<?> aClass) throws IOException {
        if (aClass == null) return Optional.empty()
        String className = aClass.getSimpleName() + ".class"
        String classPath = aClass.getResource(className).toString()
        if (classPath.startsWith("jar")) {
            String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF"
            Optional.of(new Manifest(new URL(manifestPath).openStream()))
        } else {
            Optional.empty()
        }
    }

    static Object getOrDefault(Manifest manifest, String attributeName, String defaultValue) throws NullPointerException {
        if (manifest == null) throw new NullPointerException("Manifest parameter must not be null")
        Attributes mainAttributes = manifest.getMainAttributes()
        def value = mainAttributes.find { key, value -> (key.toString() == attributeName) }
        if (value != null) {
            return value.value
        }
        return defaultValue
    }
}
