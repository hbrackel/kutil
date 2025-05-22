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

package de.macnix.util

import java.io.File
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException

/**
 * copied from http://stackoverflow.com/questions/15359702/get-location-of-jar-file
 * ================================================================================
 * Compute the absolute file path to the jar file.
 * The framework is based on http://stackoverflow.com/a/12733172/1614775
 * But that gets it right for only one of the four cases.
 *
 * @param theClass A class residing in the required jar.
 * @return A File object for the directory in which the jar file resides.
 * During testing with NetBeans, the result is ./build/classes/,
 * which is the directory containing what will be in the jar.
 */

fun <T : Any> Class<T>.getJarDirectory(theClass: Class<T>): File {
    // get an url
    val url = try {
        theClass.protectionDomain.codeSource.location
        // url is in one of two forms
        //        ./build/classes/   NetBeans test
        //        jardir/JarName.jar  froma jar
    } catch (ex: SecurityException) {
        theClass.getResource(theClass.simpleName + ".class")
        // url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
        //          file:/U:/Fred/java/Tools/UI/build/classes
        //          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
    }

    // convert to external form
    var extURL = url.toExternalForm()

    // prune for various cases
    if (extURL.endsWith(".jar"))   // from getCodeSource
        extURL = extURL.substring(0, extURL.lastIndexOf("/"))
    else {  // from getResource
        val suffix = "/" + (theClass.name).replace(".", "/") + ".class"
        extURL = extURL.replace(suffix, "")
        if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
            extURL = extURL.substring(4, extURL.lastIndexOf("/"))
    }

    // convert back to url
    val finalUrl = try {
        URI(extURL).toURL()
    } catch (mux: MalformedURLException) {
        mux.printStackTrace()
        url
        // leave url unchanged; probably does not happen
    }

    // convert url to File
    return try {
        File(finalUrl.toURI())
    } catch (ex: URISyntaxException) {
        File(finalUrl.path)
    }
}