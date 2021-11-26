package de.macnix.util.vertx.eventbus

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.MessageCodec

class GenericLocalMessageCodec<T>(val messageClass: Class<T>) : MessageCodec<T, T> {
    override fun encodeToWire(buffer: Buffer?, s: T) {
        TODO("Not yet implemented")
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer?): T {
        TODO("Not yet implemented")
    }

    override fun transform(s: T): T = s

    override fun name(): String = messageClass.name + "-messageCodec"

    override fun systemCodecID(): Byte = -1

}

fun <T> EventBus.registerDefaultCodec(
    genericMessageCodec: GenericLocalMessageCodec<T>,
    doOnRegistrationFailure: (t: Throwable) -> Unit = {}
): EventBus {
    runCatching {
        this.registerDefaultCodec(genericMessageCodec.messageClass, genericMessageCodec)
        this
    }
        .onFailure { doOnRegistrationFailure(it) }
    return this
}


