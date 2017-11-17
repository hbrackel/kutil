package de.macnix.ua.commons.url.protocols.ssh

import java.net.URL
import java.net.URLConnection
import java.net.URLStreamHandler

class SshStreamHandler : URLStreamHandler() {
    override fun openConnection(url: URL?): URLConnection {
        return SshURLConnection(url)
    }

    override fun getDefaultPort(): Int = 22
}