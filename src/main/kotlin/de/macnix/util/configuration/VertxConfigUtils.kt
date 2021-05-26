package de.macnix.util.configuration

import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.configStoreOptionsOf

object VertxConfigUtils {
    @JvmStatic
    fun storeOptionsForFile(path: String, format: String, optional: Boolean): ConfigStoreOptions {
        return configStoreOptionsOf(
            optional = optional,
            format = format,
            type = "file",
            config = JsonObject().put("path", path)
        )
    }

    @JvmStatic
    fun storeOptionsForDirectory(directory: String? = System.getenv("VERTX_CONFIG_DIR")): ConfigStoreOptions? {
        return if (directory != null) {
            ConfigStoreOptions()
                .setType("directory")
                .setOptional(true)
                .setConfig(
                    JsonObject().put("path", directory)
                        .put(
                            "filesets", JsonArray()
                                .add(JsonObject().put("pattern", "*.json"))
                                .add(JsonObject().put("pattern", "*.yaml").put("format", "yaml"))
                                .add(JsonObject().put("pattern", "*.conf").put("format", "hocon"))
                        )
                )
        } else {
            null
        }
    }
}
