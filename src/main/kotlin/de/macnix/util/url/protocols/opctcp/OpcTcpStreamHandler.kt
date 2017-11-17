package de.macnix.ua.commons.url.protocols.opctcp

import java.net.URL
import java.net.URLConnection
import java.net.URLStreamHandler

/**
 * http://stackoverflow.com/questions/26363573/registering-and-using-a-custom-java-net-url-protocol
 */
class OpcTcpStreamHandler : URLStreamHandler() {
    override fun openConnection(url: URL?): URLConnection {
        return OpcTcpURLConnection(url)
    }

    override fun getDefaultPort(): Int = 4841
}