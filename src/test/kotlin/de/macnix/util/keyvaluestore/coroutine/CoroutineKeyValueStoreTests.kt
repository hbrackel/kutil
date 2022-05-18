package de.macnix.util.keyvaluestore.coroutine


import io.vertx.kotlin.core.json.jsonObjectOf
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.*
import java.io.File
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoroutineKeyValueStoreTests {
    lateinit var kvsClient: CoroutineKeyValueStoreServer

    @Nested
    @DisplayName("a KeyValueStore")
    inner class KeyValueStore {

        @BeforeEach
        fun beforeEach() {
            runBlocking { deployKeyValueStore() }
        }

        @AfterEach
        fun afterEach() {
            runBlocking { undeployKeyValueStore() }
        }

        @Test
        fun `should return null when calling get+Type+() without a default value for an unknown key`() {
            val result = runBlocking { kvsClient.getString("unknown-key") }
            assertThat(result).isNull()
        }

        @Test
        fun `should return the default value when calling getValue with a default value for unknown key`() {
            val result = runBlocking { kvsClient.getString("unknown-key", "the default") }
            assertThat(result).isEqualTo("the default")
        }

        @Test
        fun `should return the stored String value for an existing key`() {
            val result = runBlocking {
                kvsClient.putString("theKey", "the value")
                kvsClient.getString("theKey")
            }
            assertThat(result).isEqualTo("the value")
        }

        @Test
        fun `should return the stored 'true' Boolean value for an existing key`() {
            val boolValue = runBlocking {
                kvsClient.putBoolean("theKey", true)
                kvsClient.getBoolean("theKey", false)
            }
            assertThat(boolValue).isTrue
        }

        @Test
        fun `should return the stored 'false' Boolean value for an existing key`() {
            val boolValue = runBlocking {
                kvsClient.putBoolean("theKey", false)
                kvsClient.getBoolean("theKey", true)
            }
            assertThat(boolValue).isFalse
        }

        @Test
        fun `should return the stored Int value for an existing key`() {
            val result = runBlocking {
                kvsClient.putInteger("theKey", 132)
                kvsClient.getInteger("theKey")
            }
            assertThat(result).isEqualTo(132)
        }

        @Test
        fun `should return the stored Float value for an existing key`() {
            val floatVal = 143.2f
            val result = runBlocking {
                kvsClient.putFloat("theKey", floatVal)
                kvsClient.getFloat("theKey")
            }
            assertThat(result).isCloseTo(floatVal, Offset.offset(0.00001f))
        }

        @Test
        fun `should return the stored Double value for an existing key`() {
            val doubleVal = 143.2
            val result = runBlocking {
                kvsClient.putDouble("theKey", doubleVal)
                kvsClient.getDouble("theKey")
            }
            assertThat(result).isCloseTo(doubleVal, Offset.offset(0.000000001))
        }

        @Test
        fun `should return the stored Short value for an existing key`() {
            val shortVal: Short = 123
            val result = runBlocking {
                kvsClient.putShort("theKey", shortVal)
                kvsClient.getShort("theKey")
            }
            assertThat(result).isEqualTo(shortVal)
        }

        @Test
        fun `should return the stored Byte value for an existing key`() {
            val byteVal: Byte = 123
            val result = runBlocking {
                kvsClient.putByte("theKey", byteVal)
                kvsClient.getByte("theKey")
            }
            assertThat(result).isEqualTo(byteVal)
        }

        @Test
        fun `should return the stored Long value for an existing key`() {
            val longVal = 12345678L
            val result = runBlocking {
                kvsClient.putLong("theKey", longVal)
                kvsClient.getLong("theKey")
            }
            assertThat(result).isEqualTo(longVal)
        }

        @Test
        fun `should return the stored JsonObject value for an existing key`() {
            val value = jsonObjectOf("someKey" to "someValue")
            val result = runBlocking {
                kvsClient.putJsonObject("theKey", value)
                kvsClient.getJsonObject("theKey")
            }
            assertThat(result).isEqualTo(value)
        }

        @Test
        fun `should return the removed Int value for an existing key`() {
            val result = runBlocking {
                kvsClient.putInteger("theKey", 132)
                kvsClient.remove("theKey")
            }
            assertThat(result).isEqualTo(132)
        }

        @Test
        fun `should remove a stored value for an existing key`() {
            val result = runBlocking {
                kvsClient.putInteger("theKey", 132)
                kvsClient.remove("theKey")
                kvsClient.getInteger("theKey")
            }
            assertThat(result).isNull()
        }

        @Test
        fun `should update a stored value for an existing key`() {
            val result = runBlocking {
                kvsClient.putInteger("theKey", 132)
                kvsClient.putInteger("theKey", 9876)
                kvsClient.getInteger("theKey")
            }
            assertThat(result).isEqualTo(9876)
        }


        @Nested
        @DisplayName("Untyped store access - putValue(), getValue()")
        inner class UntypedStoreAccess {

            @Test
            fun `should return the stored JsonObject value for an existing key`() {
                val value = jsonObjectOf("someKey" to "someValue")
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored String value for an existing key`() {
                val value = "the string"
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Double value for an existing key`() {
                val value = 1.234
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Float value for an existing key`() {
                val value = 1.234f
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Integer value for an existing key`() {
                val value = 143
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Long value for an existing key`() {
                val value = 312L
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Short value for an existing key`() {
                val value = (432).toShort()
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Byte value for an existing key`() {
                val value = (123).toByte()
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Boolean 'true' value for an existing key`() {
                val trueValue = true
                val result = runBlocking {
                    kvsClient.putValue("theKey", trueValue)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(trueValue)
            }

            @Test
            fun `should return the stored Boolean 'false' value for an existing key`() {
                val falseValue = false
                val falseResult = runBlocking {
                    kvsClient.putValue("theKey", falseValue)
                    kvsClient.getValue("theKey")
                }
                assertThat(falseResult).isEqualTo(falseValue)
            }

        }

        @Nested
        @DisplayName("Untyped store reads for typed writes - put<Type>(), getValue()")
        inner class TypedStoreAccessForUntypedWrites {

            @Test
            fun `should return the stored JsonObject value for an existing key`() {
                val value = jsonObjectOf("someKey" to "someValue")
                val result = runBlocking {
                    kvsClient.putJsonObject("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored String value for an existing key`() {
                val value = "the string"
                val result = runBlocking {
                    kvsClient.putString("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Double value for an existing key`() {
                val value = 1.234
                val result = runBlocking {
                    kvsClient.putDouble("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Float value for an existing key`() {
                val value = 1.234f
                val result = runBlocking {
                    kvsClient.putFloat("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Integer value for an existing key`() {
                val value = 143
                val result = runBlocking {
                    kvsClient.putInteger("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Long value for an existing key`() {
                val value = 312L
                val result = runBlocking {
                    kvsClient.putLong("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Short value for an existing key`() {
                val value = (432).toShort()
                val result = runBlocking {
                    kvsClient.putShort("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Byte value for an existing key`() {
                val value = (123).toByte()
                val result = runBlocking {
                    kvsClient.putByte("theKey", value)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Boolean 'true' value for an existing key`() {
                val trueValue = true
                val result = runBlocking {
                    kvsClient.putBoolean("theKey", trueValue)
                    kvsClient.getValue("theKey")
                }
                assertThat(result).isEqualTo(trueValue)
            }

            @Test
            fun `should return the stored Boolean 'false' value for an existing key`() {
                val falseValue = false
                val falseResult = runBlocking {
                    kvsClient.putBoolean("theKey", falseValue)
                    kvsClient.getValue("theKey")
                }
                assertThat(falseResult).isEqualTo(falseValue)
            }

        }

        @Nested
        @DisplayName("Untyped store writes with typed reads - putValue(), get<Type>()")
        inner class UntypedStoreAccessForTypedWrites {

            @Test
            fun `should return the stored JsonObject value for an existing key`() {
                val value = jsonObjectOf("someKey" to "someValue")
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getJsonObject("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored String value for an existing key`() {
                val value = "the string"
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getString("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Double value for an existing key`() {
                val value = 1.234
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getDouble("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Float value for an existing key`() {
                val value = 1.234f
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getFloat("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Integer value for an existing key`() {
                val value = 143
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getInteger("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Long value for an existing key`() {
                val value = 312L
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getLong("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Short value for an existing key`() {
                val value = (432).toShort()
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getShort("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Byte value for an existing key`() {
                val value = (123).toByte()
                val result = runBlocking {
                    kvsClient.putValue("theKey", value)
                    kvsClient.getByte("theKey")
                }
                assertThat(result).isEqualTo(value)
            }

            @Test
            fun `should return the stored Boolean 'true' value for an existing key`() {
                val trueValue = true
                val result = runBlocking {
                    kvsClient.putValue("theKey", trueValue)
                    kvsClient.getBoolean("theKey", false)
                }
                assertThat(result).isEqualTo(trueValue)
            }

            @Test
            fun `should return the stored Boolean 'false' value for an existing key`() {
                val falseValue = false
                val falseResult = runBlocking {
                    kvsClient.putValue("theKey", falseValue)
                    kvsClient.getBoolean("theKey", true)
                }
                assertThat(falseResult).isEqualTo(falseValue)
            }

        }

    }

    @Nested
    @DisplayName("a persisted KeyValueStore")
    inner class PersistedKeyValueStore {
        val getTempPath = {
            UUID.randomUUID().toString() + "-store"
        }

        @Test
        fun `should create a store file at the given path if none exists`() {
            val path = getTempPath()
            val storeFile = File(path)

            // given
            assertThat(storeFile).doesNotExist()

            // when
            runBlocking { deployKeyValueStore(path) }

            // then
            assertThat(storeFile).exists()

            // cleanup
            runBlocking { undeployKeyValueStore() }
            storeFile.delete()
        }

        @Test
        fun `should initialize the store from file at the given path if it exists`() {
            val path = getTempPath()
            val storeFile = File(path)

            // given - the file has been created and has some content
            runBlocking {
                deployKeyValueStore(path)
                kvsClient.putString("someKey", "some initial content")
                undeployKeyValueStore()
            }
            assertThat(storeFile).exists()

            // when
            runBlocking { deployKeyValueStore(path) }

            // then
            val storedValue = runBlocking {
                kvsClient.getString("someKey")
            }
            assertThat(storedValue).isEqualTo("some initial content")

            // cleanup
            runBlocking { undeployKeyValueStore() }
            storeFile.delete()
        }
    }

    @Nested
    @DisplayName("a volatile KeyValueStore")
    inner class VolatileKeyValueStore {}

    fun deployKeyValueStore(storePath: String? = null) {
        kvsClient = CoroutineKeyValueStoreServer(storePath)
    }

    fun undeployKeyValueStore() {
        kvsClient.close()
    }

}
