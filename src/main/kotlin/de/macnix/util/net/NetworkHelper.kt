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

package de.macnix.util.net

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException

class NetworkHelper private constructor() {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(NetworkHelper::class.java)

        @JvmStatic
        fun getLocalhostShortHostname(): String {
            log.trace("#getShortHostname()")
            val hostname = try {
                InetAddress.getLocalHost().getShortHostName()
            } catch (e: UnknownHostException) {
                log.error("#getShortHostname(): FAILED to retrieve hostname ({})", e.message)
                "localhost"
            }
            log.debug("#getShortHostname() => {}", hostname)
            return hostname
        }

        @JvmStatic
        fun getIP4HostAddresses(): Array<String> {
            return try {
                NetworkInterface.getNetworkInterfaces().toList()
                        .flatMap { it.inetAddresses.toList() }
                        .filter { it.address.size == 4 }
                        .map { it.hostAddress }
                        .toTypedArray()
            } catch (e: SocketException) {
                log.error(e.message)
                emptyArray<String>()
            }
        }
    }
}