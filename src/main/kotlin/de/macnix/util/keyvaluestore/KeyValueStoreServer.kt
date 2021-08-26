package de.macnix.util.keyvaluestore

import de.macnix.util.vertx.vertxactor.verticle.AbstractBehaviourVerticle
import de.macnix.util.vertx.vertxactor.verticle.Behavior
import de.macnix.util.vertx.vertxactor.verticle.ReceiveBuilder
import io.vertx.core.Future
import io.vertx.core.eventbus.Message
import io.vertx.core.file.FileSystem
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.await

class KeyValueStoreServer : AbstractBehaviourVerticle<JsonObject>() {
    private var storePath: String? = null
    private var store = JsonObject()
    lateinit private var fs: FileSystem

    override suspend fun start() {
        super.start()
        storePath = config.getString(STORE_PATH_KEY)
        logger.info("config[\"storePath\"]           : {}", storePath)

        fs = vertx.fileSystem()
        createStoreFileIfNotExists()
        initializeStoreFromStoreFile()

    }

    private suspend fun createStoreFileIfNotExists() {
        if (storePath != null) {
            try {
                logger.info("creating new store file at {}", storePath)
                if (!fs.exists(storePath).await()) {
                    flushStore()?.await()
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

    private fun flushStore(): Future<Void>? {
        return if (storePath != null) {
            val future = fs.writeFile(storePath, store.toBuffer())

            future.onSuccess {
                logger.debug("key-value-store successfully written to file '{}'", storePath)
            }
            future.onFailure { t ->
                logger.error(
                    "failed to write key-value-store to file '{}' => {}",
                    storePath,
                    t.message
                )
            }
            future
        } else {
            null
        }
    }

    override fun createReceive(): Behavior<JsonObject> {
        return ReceiveBuilder.buildReceive { message, behavior ->
            handleMessage(message)
            behavior
        }
    }

    private fun handleMessage(message: Message<JsonObject>) {
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
                    flushStore()
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
                "handleMessage() - exception caught when processing message with body={} => {}",
                body,
                e.message
            )
        }
    }

    override suspend fun stop() {
        flushStore()?.await()   // need to make sure, that the store has been completely written to file before the verticle stops
        super.stop()
    }

    companion object {
        const val STORE_PATH_KEY = "storePath"
    }
}


