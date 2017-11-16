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

import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object ManifestHelperSpec : Spek({
    describe("a ManifestHelper") {

        given("a class from a jar") {
            val clazz = Spek::class.java

            on("getting the manifest for the class") {
                val manifest = ManifestHelper.getManifestForClass(clazz)
                it("should return the manifest") {
                    assertThat(manifest, notNullValue())
                }
            }
        }

        given("a class which is not in a jar") {
            class TestClass {}

            on("getting the manifest for the class") {
                val manifest = ManifestHelper.getManifestForClass(TestClass::class.java)

                it("should return null") {
                    assertThat(manifest, org.hamcrest.CoreMatchers.nullValue())
                }
            }

        }
    }
})