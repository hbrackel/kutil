package de.macnix.util.keyvaluestore.vertx

import de.macnix.util.vertx.vertxactor.verticle.AbstractBehaviourVerticle
import de.macnix.util.vertx.vertxactor.verticle.Behavior
import de.macnix.util.vertx.vertxactor.verticle.ReceiveBuilder
import io.vertx.core.eventbus.Message
import io.vertx.core.file.FileSystem
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.async

class KeyValueStoreServerVerticle : AbstractBehaviourVerticle<JsonObject>() {
    private var storePath: String? = null
    private var store = JsonObject()
    private lateinit var fs: FileSystem

    override suspend fun doAfterStart() {
        storePath = config.getString(STORE_PATH_KEY)
        logger.info("config[\"storePath\"]           : {}", storePath)

        fs = vertx.fileSystem()
        createStoreFileIfNotExists()
        initializeStoreFromStoreFile()
    }

    override suspend fun registerMessageCodecs() {}

    private suspend fun createStoreFileIfNotExists() {
        if (storePath != null) {
            try {
                if (!fs.exists(storePath).await()) {
                    logger.info("creating new store file at {}", storePath)
                    flushStore()
                }
            } catch (e: Exception) {
                logger.error("Failed to create store file at {} => {}", storePath, e.message)
                throw e
            }
        }
    }

    private suspend fun initializeStoreFromStoreFile() {
        if (storePath != null) {
            logger.info("initializing store from file at {}", storePath)
            try {
                val buffer = fs.readFile(storePath).await()
                val jsonFromStore = JsonObject(buffer)
                store = jsonFromStore
            } catch (e: Exception) {
                logger.error("Failed to initialize store from file at {} => {}", storePath, e.message)
                logger.warn("Creating a new empty store")
                store = JsonObject()
            }
        }

    }

    private suspend fun flushStore() {
        if (storePath != null) {
            try {
                fs.writeFile(storePath, store.toBuffer()).await()
                logger.debug("key-value-store successfully written to file '{}'", storePath)
            } catch (t: Throwable) {
                logger.error(
                    "failed to write key-value-store to file '{}' => {}", storePath, t.message
                )
            }
        }
    }

    override fun createReceive(): Behavior<JsonObject> {
        return ReceiveBuilder.buildReceive { message, behavior ->
            handleMessage(message)
            behavior
        }
    }

    private suspend fun handleMessage(message: Message<JsonObject>) {
        val body = message.body()
        val action: String? = body.getString("action")
        val key: String? = body.getString("key")

        logger.debug("handleMessage(body: {}", body)

//        println("store  : $store")
//        println("message: ${message.body()}")
        try {
            when (action) {
                "get" -> {
                    val value: JsonObject? = store.getJsonObject(key)
                    message.reply(value)
                }

                "put" -> {
                    store.put(key, body.getJsonObject("value"))
                    async { flushStore() }
                    message.reply(null)
                }

                "remove" -> {
                    val removed: JsonObject? = store.remove(key) as? JsonObject
                    message.reply(removed)
                }

                else -> {
                    message.fail(-1, "Invalid action='$action'")
                }
            }
        } catch (e: Exception) {
            logger.error(
                "handleMessage() - exception caught when processing message with body={} => {}", body, e.message
            )
        }
    }

    override suspend fun doBeforeStop() {
        flushStore()
    }

    companion object {
        const val STORE_PATH_KEY = "storePath"
        const val VALUE_KEY = "value"
    }
}


