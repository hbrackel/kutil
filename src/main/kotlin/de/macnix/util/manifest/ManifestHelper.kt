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

package de.macnix.util.manifest

import java.io.IOException
import java.net.URL
import java.util.jar.Manifest

class ManifestHelper private constructor() {
    companion object {

        /**
         * Inspired by the solution in <a href="http://stackoverflow.com/questions/3777055/reading-manifest-mf-file-from-jar-file-using-java">http://stackoverflow.com/questions/3777055/reading-manifest-mf-file-from-jar-file-using-java</a>
         */
        @JvmStatic
        @Throws(IOException::class)
        fun <T> getManifestForClass(aClass: Class<T>): Manifest? {
            val className = aClass.simpleName + ".class"
            val classPath = aClass.getResource(className)?.toString()

            if (classPath != null && classPath.startsWith("jar")) {
                val manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF"
                return Manifest(URL(manifestPath).openStream())
            } else {
                return null
            }
        }
    }
}
