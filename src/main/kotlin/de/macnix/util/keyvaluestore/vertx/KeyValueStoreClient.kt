package de.macnix.util.keyvaluestore.vertx

import de.macnix.util.keyvaluestore.KeyValueStore
import de.macnix.util.keyvaluestore.vertx.KeyValueStoreServerVerticle.Companion.VALUE_KEY
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.await
import org.slf4j.LoggerFactory

class KeyValueStoreClient(vertx: Vertx, private val eventBusAddress: String) : KeyValueStore {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val eventBus = vertx.eventBus()

    init {
        logger.info("<init> - initializing {}", javaClass.simpleName)
        logger.info("config[\"eventBusAddress\"]     : {}", eventBusAddress)
    }

    override suspend fun getString(key: String, default: String?): String? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getString(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getString(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putString(key: String, value: String): String {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putString(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    override suspend fun putInteger(key: String, value: Int): Int {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putInteger(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    override suspend fun getInteger(key: String, default: Int?): Int? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(VALUE_KEY)?.toInt()
        } catch (e: Exception) {
            logger.warn("Failed to getInteger(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putFloat(key: String, value: Float): Float {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putFloat(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    override suspend fun getFloat(key: String, default: Float?): Float? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(VALUE_KEY)?.toFloat()
        } catch (e: Exception) {
            logger.warn("Failed to getFloat(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putDouble(key: String, value: Double): Double {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putDouble(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    override suspend fun getDouble(key: String, default: Double?): Double? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(VALUE_KEY)?.toDouble()
        } catch (e: Exception) {
            logger.warn("Failed to getDouble(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putLong(key: String, value: Long): Long {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putLong(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    override suspend fun getLong(key: String, default: Long?): Long? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(VALUE_KEY)?.toLong()
        } catch (e: Exception) {
            logger.warn("Failed to getLong(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putShort(key: String, value: Short): Short {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putShort(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    override suspend fun getShort(key: String, default: Short?): Short? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(VALUE_KEY)?.toShort()
        } catch (e: Exception) {
            logger.warn("Failed to getShort(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putByte(key: String, value: Byte): Byte {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putByte(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    override suspend fun getByte(key: String, default: Byte?): Byte? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(VALUE_KEY)?.toByte()
        } catch (e: Exception) {
            logger.warn("Failed to getByte(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putBoolean(key: String, value: Boolean): Boolean {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putBoolean(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    override suspend fun getBoolean(key: String, default: Boolean): Boolean {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getBoolean(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getBoolean(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putJsonObject(key: String, value: JsonObject): JsonObject {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putJsonObject(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    override suspend fun getJsonObject(key: String, default: JsonObject?): JsonObject? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getJsonObject(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getJsonObject(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun putJsonArray(key: String, value: JsonArray): JsonArray {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        VALUE_KEY to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putJsonArray(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    override suspend fun getJsonArray(key: String, default: JsonArray?): JsonArray? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getJsonArray(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to getJsonArray(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    override suspend fun remove(key: String): Any? {
        val removedValue = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "remove",
                    "key" to key
                )
            ).await().body()?.get<Any?>(VALUE_KEY)
        } catch (e: Exception) {
            logger.warn("Failed to remove(key: '{}') => {}", key, e.message)
            null
        }
        return removedValue
    }

    override suspend fun getValue(key: String): Any? {
        return try {
            val storeValue: JsonObject? = eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()
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

    override suspend fun putValue(key: String, value: Any): Any {
        try {
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
        } catch (e: Exception) {
            logger.warn("Failed to putValue(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    override fun close() {
        // no resources to be released at the client side
    }
}
