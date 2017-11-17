package de.macnix.ua.commons.url.protocols.ssh

import java.io.IOException
import java.net.URL
import java.net.URLConnection


open class SshURLConnection constructor(url: URL?) : URLConnection(url) {

    @Throws(IOException::class)
    override fun connect() {
        println("SshURL connected!")
    }


}