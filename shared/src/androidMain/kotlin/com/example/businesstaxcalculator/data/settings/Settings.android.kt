package com.example.businesstaxcalculator.data.settings

import android.content.SharedPreferences
import com.example.businesstaxcalculator.domain.settings.AppSettings

fun createAppSettings(preferences: SharedPreferences): AppSettings =
    StoredAppSettings(AndroidPreferencesStore(preferences))

private class AndroidPreferencesStore(
    private val preferences: SharedPreferences,
) : PreferencesStore {
    // Existing Android preferences can contain native integers, booleans and rates.
    override fun read(key: String): String? = preferences.all[key]?.toString()

    override fun write(values: Map<String, String?>) {
        val editor = preferences.edit()
        values.forEach { (key, value) ->
            if (value == null) editor.remove(key) else editor.putString(key, value)
        }
        editor.apply()
    }
}
