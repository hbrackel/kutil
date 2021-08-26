package de.macnix.util.keyvaluestore

import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.await
import org.slf4j.LoggerFactory

class KeyValueStoreClient(vertx: Vertx, private val eventBusAddress: String) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val eventBus = vertx.eventBus()

    init {
        logger.info("<init> - initializing {}", javaClass.simpleName)
        logger.info("config[\"eventBusAddress\"]     : {}", eventBusAddress)
    }

    suspend fun getString(key: String, default: String? = null): String? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getString(key)
        } catch (e: Exception) {
            logger.warn("Failed to getString(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun putString(key: String, value: String): String {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putString(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    suspend fun putInteger(key: String, value: Int): Int {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putInteger(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    suspend fun getInteger(key: String, default: Int? = null): Int? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(key)?.toInt()
        } catch (e: Exception) {
            logger.warn("Failed to getInteger(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun putFloat(key: String, value: Float): Float {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putFloat(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    suspend fun getFloat(key: String, default: Float? = null): Float? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(key)?.toFloat()
        } catch (e: Exception) {
            logger.warn("Failed to getFloat(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun putDouble(key: String, value: Double): Double {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putDouble(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }

    suspend fun getDouble(key: String, default: Double? = null): Double? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(key)?.toDouble()
        } catch (e: Exception) {
            logger.warn("Failed to getDouble(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun putLong(key: String, value: Long): Long {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putLong(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    suspend fun getLong(key: String, default: Long? = null): Long? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(key)?.toLong()
        } catch (e: Exception) {
            logger.warn("Failed to getLong(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun putShort(key: String, value: Short): Short {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putShort(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    suspend fun getShort(key: String, default: Short? = null): Short? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(key)?.toShort()
        } catch (e: Exception) {
            logger.warn("Failed to getShort(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun putByte(key: String, value: Byte): Byte {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putByte(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    suspend fun getByte(key: String, default: Byte? = null): Byte? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getNumber(key)?.toByte()
        } catch (e: Exception) {
            logger.warn("Failed to getByte(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun putBoolean(key: String, value: Boolean): Boolean {
        try {
            eventBus.request<Unit>(
                eventBusAddress, jsonObjectOf(
                    "action" to "put",
                    "key" to key,
                    "value" to jsonObjectOf(
                        key to value,
                        "className" to value.javaClass.name
                    )
                )
            ).await()
        } catch (e: Exception) {
            logger.warn("Failed to putBoolean(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }


    suspend fun getBoolean(key: String, default: Boolean? = null): Boolean? {
        val value = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()?.getBoolean(key)
        } catch (e: Exception) {
            logger.warn("Failed to getBoolean(key: '{}', default: '{}') => {}", key, default, e.message)
            null
        }
        return value ?: default
    }

    suspend fun remove(key: String): Any? {
        val removedValue = try {
            eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "remove",
                    "key" to key
                )
            ).await().body()?.get<Any?>(key)
        } catch (e: Exception) {
            logger.warn("Failed to remove(key: '{}') => {}", key, e.message)
            null
        }
        return removedValue
    }

    suspend fun getValue(key: String): Any? {
        return try {
            val storeValue: JsonObject? = eventBus.request<JsonObject?>(
                eventBusAddress, jsonObjectOf(
                    "action" to "get",
                    "key" to key
                )
            ).await().body()
            if (storeValue != null) {
                val className: String? = storeValue.getString("className")
                when (className) {
                    java.lang.Double::class.java.name, Double::class.java.name -> storeValue.getDouble(key)
                    java.lang.Float::class.java.name, Float::class.java.name -> storeValue.getFloat(key)
                    java.lang.Byte::class.java.name, Byte::class.java.name -> storeValue.getInteger(key).toByte()
                    java.lang.Integer::class.java.name, Int::class.java.name -> storeValue.getInteger(key)
                    java.lang.Long::class.java.name, Long::class.java.name -> storeValue.getLong(key)
                    java.lang.Short::class.java.name, Short::class.java.name -> storeValue.getInteger(key)?.toShort()
                    java.lang.Boolean::class.java.name, Boolean::class.java.name -> storeValue.getBoolean(key)
                    java.lang.String::class.java.name, String::class.java.name -> storeValue.getString(key)
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

    suspend fun putValue(key: String, value: Any): Any {
        try {
            val clazzName = value.javaClass.name
            when (clazzName) {
                java.lang.Double::class.java.name, Double::class.java.name -> putDouble(key, value as Double)
                java.lang.Float::class.java.name, Float::class.java.name -> putFloat(key, value as Float)
                java.lang.Byte::class.java.name, Byte::class.java.name -> putByte(key, value as Byte)
                java.lang.Integer::class.java.name, Int::class.java.name -> putInteger(key, value as Int)
                java.lang.Long::class.java.name, Long::class.java.name -> putLong(key, value as Long)
                java.lang.Short::class.java.name, Short::class.java.name -> putShort(key, value as Short)
                java.lang.Boolean::class.java.name, Boolean::class.java.name -> putBoolean(key, value as Boolean)
                java.lang.String::class.java.name, String::class.java.name -> putString(key, value as String)
            }
        } catch (e: Exception) {
            logger.warn("Failed to putValue(key: '{}', value: '{}') => {}", key, value, e.message)
        }
        return value
    }
}
