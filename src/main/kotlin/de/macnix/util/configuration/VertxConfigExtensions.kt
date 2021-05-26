package de.macnix.util.configuration

import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.configStoreOptionsOf

fun Vertx.storeOptionsForFile(path: String, format: String, optional: Boolean): ConfigStoreOptions {
    return configStoreOptionsOf(
        optional = optional,
        format = format,
        type = "file",
        config = JsonObject().put("path", path)
    )
}

fun Vertx.storeOptionsForEnvFile(envVariableName: String = "VERTX_CONFIG_PATH"): ConfigStoreOptions? {
    val envFilePath: String? = System.getenv(envVariableName)
    val format = when (envFilePath?.split(".")?.lastOrNull()?.lowercase()) {
        "yaml", "yml" -> "yaml"
        "json" -> "json"
        "conf" -> "hocon"
        else -> null
    }
    return if (format != null) {
        return configStoreOptionsOf(
            optional = true,
            format = format,
            type = "file",
            config = JsonObject().put("path", envFilePath)
        )
    } else null

}
