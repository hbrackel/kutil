package de.macnix.util.keyvaluestore

import io.vertx.core.json.JsonObject

interface KeyValueStore : AutoCloseable {
    suspend fun getString(key: String, default: String? = null): String?
    suspend fun putString(key: String, value: String): String
    suspend fun putInteger(key: String, value: Int): Int
    suspend fun getInteger(key: String, default: Int? = null): Int?
    suspend fun putFloat(key: String, value: Float): Float
    suspend fun getFloat(key: String, default: Float? = null): Float?
    suspend fun putDouble(key: String, value: Double): Double
    suspend fun getDouble(key: String, default: Double? = null): Double?
    suspend fun putLong(key: String, value: Long): Long
    suspend fun getLong(key: String, default: Long? = null): Long?
    suspend fun putShort(key: String, value: Short): Short
    suspend fun getShort(key: String, default: Short? = null): Short?
    suspend fun putByte(key: String, value: Byte): Byte
    suspend fun getByte(key: String, default: Byte? = null): Byte?
    suspend fun putBoolean(key: String, value: Boolean): Boolean
    suspend fun getBoolean(key: String, default: Boolean? = null): Boolean?
    suspend fun remove(key: String): Any?
    suspend fun getValue(key: String): Any?
    suspend fun putValue(key: String, value: Any): Any
    suspend fun putJsonObject(key: String, value: JsonObject): JsonObject
    suspend fun getJsonObject(key: String, default: JsonObject? = null): JsonObject?
}
