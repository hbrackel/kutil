package de.macnix.util.keyvaluestore.coroutine

import de.macnix.util.keyvaluestore.KeyValueStore
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.jsonObjectOf
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class CoroutineKeyValueStoreServer(storePath: String? = null) : KeyValueStore, CoroutineScope {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val job = Job()

    private val storeDispatcherExecutor = Executors.newSingleThreadExecutor()
    private val storeDispatcher = storeDispatcherExecutor.asCoroutineDispatcher()
    private val fileDispatcherExecutor = Executors.newSingleThreadExecutor()
    private val fileDispatcher = storeDispatcherExecutor.asCoroutineDispatcher()
    private var kvs = JsonObject()

    private val storeFile = storePath?.let { File(storePath) }

    override val coroutineContext: CoroutineContext
        get() = job + storeDispatcher

    init {
        logger.info("<init> - initializing {}", javaClass.simpleName)
        coroutineContext.job.invokeOnCompletion {
            logger.info("{} job completed", javaClass.simpleName)
            storeDispatcher.close()
            fileDispatcher.close()
        }

        runBlocking {
            createStoreFileIfNotExists()
            initializeStoreFromStoreFile()
        }
    }

    private suspend fun createStoreFileIfNotExists() = withContext(fileDispatcher) {
        if (storeFile != null) {
            try {
                logger.info("creating new store file at {}", storeFile.path)
                if (!storeFile.exists()) {
                    writeStoreToFile()
                }
            } catch (e: Exception) {
                logger.error("Failed to create store file at {} => {}", storeFile.path, e.message)
                throw e
            }
        }

    }

    private suspend fun writeStoreToFile() = withContext(fileDispatcher) {
        if (storeFile != null) {
            val storeContent = kvs.encodePrettily()
            try {
                storeFile.writeText(storeContent)
            } catch (e: Exception) {
                logger.error("failed to write key-value-store to file '{}' => {}", storeFile.path, e.message)
            }
        }
    }

    private suspend fun initializeStoreFromStoreFile() = withContext(fileDispatcher) {
        if (storeFile != null) {
            logger.info("initializing store from file at {}", storeFile.path)
            kvs = try {
                val kvsContext = storeFile.readText()
                JsonObject(kvsContext)
            } catch (e: Exception) {
                logger.error("Failed to initialize store from file at {} => {}", storeFile.path, e.message)
                logger.warn("Creating a new empty store")
                JsonObject()
            }
        }

    }

    override suspend fun getString(key: String, default: String?): String? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getString(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getString(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putString(key: String, value: String): String = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun putInteger(key: String, value: Int): Int = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getInteger(key: String, default: Int?): Int? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getNumber(VALUE_KEY)?.toInt()
        } catch (e: Exception) {
            logger.warn("Failed to getInteger(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putFloat(key: String, value: Float): Float = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getFloat(key: String, default: Float?): Float? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getNumber(VALUE_KEY)?.toFloat()
        } catch (e: Exception) {
            logger.warn("Failed to getFloat(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putDouble(key: String, value: Double): Double = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getDouble(key: String, default: Double?): Double? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getNumber(VALUE_KEY)?.toDouble()
        } catch (e: Exception) {
            logger.warn("Failed to getDouble(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putLong(key: String, value: Long): Long = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getLong(key: String, default: Long?): Long? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getNumber(VALUE_KEY)?.toLong()
        } catch (e: Exception) {
            logger.warn("Failed to getLong(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putShort(key: String, value: Short): Short = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getShort(key: String, default: Short?): Short? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getNumber(VALUE_KEY)?.toShort()
        } catch (e: Exception) {
            logger.warn("Failed to getShort(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putByte(key: String, value: Byte): Byte = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getByte(key: String, default: Byte?): Byte? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getNumber(VALUE_KEY)?.toByte()
        } catch (e: Exception) {
            logger.warn("Failed to getByte(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putJsonObject(key: String, value: JsonObject): JsonObject = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getJsonObject(key: String, default: JsonObject?): JsonObject? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getJsonObject(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getJsonObject(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putJsonArray(key: String, value: JsonArray): JsonArray = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getJsonArray(key: String, default: JsonArray?): JsonArray? = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getJsonArray(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getJsonArray(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun putBoolean(key: String, value: Boolean): Boolean = withContext(storeDispatcher) {
        kvs.put(key, jsonObjectOf(VALUE_KEY to value, "className" to value.javaClass.name))
        writeStoreToFile()
        value
    }

    override suspend fun getBoolean(key: String, default: Boolean): Boolean = withContext(storeDispatcher) {
        val value = try {
            kvs.getJsonObject(key)?.getBoolean(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getBoolean(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        value ?: default
    }

    override suspend fun remove(key: String): Any? = withContext(storeDispatcher) {
        val removed: JsonObject? = kvs.remove(key) as? JsonObject
        writeStoreToFile()
        removed?.get(VALUE_KEY)
    }

    override suspend fun getValue(key: String): Any? = withContext(storeDispatcher) {
        try {
            val storeValue: JsonObject? = kvs[key]

            if (storeValue != null) {
                when (storeValue.getString("className")) {
                    java.lang.Double::class.java.name, Double::class.java.name -> storeValue.getDouble(VALUE_KEY)
                    java.lang.Float::class.java.name, Float::class.java.name -> storeValue.getFloat(VALUE_KEY)
                    java.lang.Byte::class.java.name, Byte::class.java.name -> storeValue.getInteger(VALUE_KEY).toByte()
                    java.lang.Integer::class.java.name, Int::class.java.name -> storeValue.getInteger(VALUE_KEY)
                    java.lang.Long::class.java.name, Long::class.java.name -> storeValue.getLong(VALUE_KEY)
                    java.lang.Short::class.java.name, Short::class.java.name -> storeValue.getInteger(VALUE_KEY)
                        ?.toShort()

                    java.lang.Boolean::class.java.name, Boolean::class.java.name -> storeValue.getBoolean(VALUE_KEY)
                    java.lang.String::class.java.name, String::class.java.name -> storeValue.getString(VALUE_KEY)
                    JsonObject::class.java.name -> storeValue.getJsonObject(VALUE_KEY)
                    JsonArray::class.java.name -> storeValue.getJsonArray(VALUE_KEY)
                    else -> null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            logger.warn("Failed to getValue(key: '{}') => {}", key, e.message)
            null
        }
    }

    override suspend fun putValue(key: String, value: Any): Any = withContext(storeDispatcher) {
        when (value.javaClass.name) {
            java.lang.Double::class.java.name, Double::class.java.name -> putDouble(key, value as Double)
            java.lang.Float::class.java.name, Float::class.java.name -> putFloat(key, value as Float)
            java.lang.Byte::class.java.name, Byte::class.java.name -> putByte(key, value as Byte)
            java.lang.Integer::class.java.name, Int::class.java.name -> putInteger(key, value as Int)
            java.lang.Long::class.java.name, Long::class.java.name -> putLong(key, value as Long)
            java.lang.Short::class.java.name, Short::class.java.name -> putShort(key, value as Short)
            java.lang.Boolean::class.java.name, Boolean::class.java.name -> putBoolean(key, value as Boolean)
            java.lang.String::class.java.name, String::class.java.name -> putString(key, value as String)
            JsonObject::class.java.name -> putJsonObject(key, value as JsonObject)
            JsonArray::class.java.name -> putJsonArray(key, value as JsonArray)
        }
        writeStoreToFile()
        value
    }

    override fun close() {
        logger.info("close()")
        storeDispatcherExecutor.shutdownNow()
        fileDispatcherExecutor.shutdownNow()
        job.cancel()
    }

    companion object {
        const val VALUE_KEY = "value"
    }
}
