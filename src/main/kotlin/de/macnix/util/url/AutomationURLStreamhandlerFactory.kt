package de.macnix.ua.commons.url

import de.macnix.ua.commons.url.protocols.opctcp.OpcTcpStreamHandler
import de.macnix.ua.commons.url.protocols.ssh.SshStreamHandler
import java.net.URLStreamHandler
import java.net.URLStreamHandlerFactory


/**
 *     http://stackoverflow.com/questions/26363573/registering-and-using-a-custom-java-net-url-protocol
 */
class AutomationURLStreamhandlerFactory : URLStreamHandlerFactory {
    override fun createURLStreamHandler(protocol: String): URLStreamHandler? =
            when (protocol) {
                "opc.tcp" -> OpcTcpStreamHandler()
                "ssh" -> SshStreamHandler()
                else -> null
            }
}