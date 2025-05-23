package de.macnix.util.keyvaluestore.vertx

import arrow.core.Either.Companion.catch
import arrow.core.some
import de.macnix.util.vertx.eventbus.EventBusAddress
import de.macnix.util.vertx.vertxactor.verticle.AbstractBehaviourVerticle
import de.macnix.util.vertx.vertxactor.verticle.Behavior
import de.macnix.util.vertx.vertxactor.verticle.ReceiveBuilder
import io.vertx.core.eventbus.Message
import io.vertx.core.file.FileSystem
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class KeyValueStoreServerVerticle(private val storePath: String, eventBusAddress: EventBusAddress) :
    AbstractBehaviourVerticle<JsonObject>(eventBusAddress.some()) {
    private var store = JsonObject()
    private lateinit var fs: FileSystem

    override suspend fun doAfterStart() {
        logger.info("config[\"storePath\"]           : {}", storePath)

        fs = vertx.fileSystem()
        createStoreFileIfNotExists()
        initializeStoreFromStoreFile()
    }

    override suspend fun registerMessageCodecs() {}

    private suspend fun createStoreFileIfNotExists() {
        withContext(Dispatchers.IO) {
            val storeFile = File(storePath)
            if (!storeFile.exists()) {
                logger.info("creating new store file at {}", storePath)
                catch { storeFile.parentFile?.mkdirs() }
                    .onRight { flushStore() }
                    .onLeft {
                        logger.error("Failed to create store file at {} => {}", storePath, it.message)
                        throw it
                    }

            }
        }
    }

    private suspend fun initializeStoreFromStoreFile() {
        logger.info("initializing store from file at {}", storePath)
        store = try {
            val buffer = fs.readFile(storePath).coAwait()
            val jsonFromStore = JsonObject(buffer)
            jsonFromStore
        } catch (e: Exception) {
            logger.error("Failed to initialize store from file at {} => {}", storePath, e.message)
            logger.warn("Creating a new empty store")
            JsonObject()
        }
    }

    private suspend fun flushStore() {
        try {
            fs.writeFile(storePath, store.toBuffer()).coAwait()
            logger.debug("key-value-store successfully written to file '{}'", storePath)
        } catch (t: Throwable) {
            logger.error(
                "failed to write key-value-store to file '{}' => {}", storePath, t.message
            )
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
                    launch { flushStore() }
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
        const val VALUE_KEY = "value"
    }
}


