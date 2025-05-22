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

package de.macnix.util.url

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.net.URI


class CustomURLs {
    init {
        URLStreamHandlerFactoryManager
    }

    @Test
    fun createSslURL() {
        val sshUrl = URI("ssh", null, "127.0.0.1", 123, "/myFile", null, null).toURL()
        assertAll("URL creation",
                Executable { assertEquals("ssh", sshUrl.protocol) },
                Executable { assertEquals("127.0.0.1", sshUrl.host) },
                Executable { assertEquals(123, sshUrl.port) },
                Executable { assertEquals("/myFile", sshUrl.file) }
        )
    }

    @Test
    fun createOpcTcpURL() {
        val opcTcpUrlSpec = "opc.tcp://localhost:4880/AGS"
        val url = URI.create(opcTcpUrlSpec).toURL()
        assertAll("URL creation",
                Executable { assertEquals("opc.tcp", url.protocol) },
                Executable { assertEquals("localhost", url.host) },
                Executable { assertEquals(4880, url.port) },
                Executable { assertEquals("/AGS", url.file) }
        )
    }

    @Test
    fun cannotUseBracketsInPlaceholderForHost() {
        val specWithPlaceholder = "opc.tcp://[host]:4880"

        val ex = assertThrows(IllegalArgumentException::class.java, { URI.create(specWithPlaceholder).toURL() })
        assertEquals("Malformed IPv6 address at index 11: opc.tcp://[host]:4880", ex.message)
    }

}