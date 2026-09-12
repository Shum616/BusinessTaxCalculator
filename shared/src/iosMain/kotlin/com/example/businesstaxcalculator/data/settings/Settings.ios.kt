package com.example.businesstaxcalculator.data.settings

import com.example.businesstaxcalculator.domain.settings.AppSettings
import platform.Foundation.NSUserDefaults

fun createAppSettings(): AppSettings =
    StoredAppSettings(IosPreferencesStore(NSUserDefaults.standardUserDefaults))

private class IosPreferencesStore(private val defaults: NSUserDefaults) : PreferencesStore {
    override fun read(key: String): String? = defaults.stringForKey(key)

    override fun write(values: Map<String, String?>) {
        values.forEach { (key, value) ->
            if (value == null) defaults.removeObjectForKey(key)
            else defaults.setObject(value, forKey = key)
        }
    }
}
