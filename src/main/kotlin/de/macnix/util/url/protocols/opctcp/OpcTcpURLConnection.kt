package de.macnix.ua.commons.url.protocols.opctcp

import java.io.IOException
import java.net.URL
import java.net.URLConnection


open class OpcTcpURLConnection constructor(url: URL?) : URLConnection(url) {

    @Throws(IOException::class)
    override fun connect() {
        println("OpcTcp URL connected!")
    }

}