package de.macnix.util.vertx.vertxactor.verticle

import io.vertx.core.eventbus.Message

interface Behavior<T> {
    suspend fun receive(msg: Message<T>): Behavior<T>
}
