package de.macnix.util.vertx.vertxactor.verticle

import io.vertx.core.eventbus.Message
import org.slf4j.LoggerFactory

object ReceiveBuilder {
    private val logger = LoggerFactory.getLogger(ReceiveBuilder::class.java)

    fun <T> buildReceive(onMessage: suspend (msg: Message<T>, thisBehavior: Behavior<T>) -> Behavior<T>): Behavior<T> {
        return object : Behavior<T> {
            override suspend fun receive(msg: Message<T>): Behavior<T> {
                return try {
                    onMessage.invoke(msg, this)
                } catch (e: Exception) {
                    logger.error("error in receive(msg[body]={}) - {}", msg.body(), e.message)
                    this
                }
            }
        }
    }
}
