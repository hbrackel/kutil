/*
 * Copyright (c) 2017 Hans-Uwe Brackel
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.macnix.util.preferences

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences

//TODO use kotlin Delegate
class UserPreferences(clazz: Class<*>) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserPreferences::class.java)
    }

    private val prefix = "${clazz.simpleName}."
    private val prefs: Preferences = Preferences.userNodeForPackage(clazz)

    private fun flush() {
        try {
            prefs.flush()
        } catch (e: BackingStoreException) {
            log.error("Error writing Preferences: {}", e.message)
        }
    }

    fun get(key: String, defaultValue: String? = null): String? {
        return prefs.get(prefix + key, defaultValue)
    }

    fun set(key: String, value: String?) {
        if (value == null) {
            prefs.remove(prefix + key)
        } else {
            prefs.put(prefix + key, value)
        }
        flush()
    }

    fun remove(key: String) {
        prefs.remove(prefix + key)
        flush()
    }
}
