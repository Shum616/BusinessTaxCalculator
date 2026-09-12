package com.example.businesstaxcalculator.data.settings

internal interface PreferencesStore {
    fun read(key: String): String?

    // Null removes the key. Numeric values are encoded as exact decimal strings.
    fun write(values: Map<String, String?>)
}
