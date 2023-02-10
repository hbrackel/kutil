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

package de.macnix.util.jar

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ManifestHelper")
@Disabled   // no jar functionality with Java 11
class ManifestHelperTests {

    @Nested
    @DisplayName("getManifestForClass")
    inner class GetManifestForClass {
        @Test
        fun `should return the jar's manifest when called with a class in a jar`() {
            val clazz = Exception::class.java

            val manifest = ManifestHelper.getManifestForClass(clazz)
            assertThat(manifest).isNotNull
        }

        @Test
        fun `should return null when called with a class not in a jar`() {
            class TestClass

            val clazz = TestClass::class.java

            val manifest = ManifestHelper.getManifestForClass(clazz)
            assertThat(manifest).isNull()
        }

    }
}
