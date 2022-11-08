package de.macnix.util.vertx.eventbus

@JvmInline
value class EventBusAddress(val address: String) {
    init {
        require(address.isNotBlank()) { "address must not be blank" }
    }

    companion object
}
