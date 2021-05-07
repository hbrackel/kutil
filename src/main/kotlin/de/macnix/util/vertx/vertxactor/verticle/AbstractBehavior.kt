package de.macnix.util.vertx.vertxactor.verticle

interface AbstractBehavior<T> {
    val behavior: Behavior<T>
    fun createReceive(): Behavior<T>
}
