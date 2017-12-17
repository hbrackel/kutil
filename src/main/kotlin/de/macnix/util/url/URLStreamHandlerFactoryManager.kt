package de.macnix.util.url

import java.net.URL

class URLStreamHandlerFactoryManager {
    companion object Manager {
        init {
            URL.setURLStreamHandlerFactory(AutomationURLStreamhandlerFactory())
        }
    }
}