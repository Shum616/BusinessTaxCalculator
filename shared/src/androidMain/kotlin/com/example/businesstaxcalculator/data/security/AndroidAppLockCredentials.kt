package com.example.businesstaxcalculator.data.security

import android.content.SharedPreferences
import com.example.businesstaxcalculator.domain.security.AppLockCredentials

// Retains existing Android credentials until the separate secure-storage migration.
class AndroidAppLockCredentials(private val preferences: SharedPreferences) : AppLockCredentials {
    override fun verify(password: String): Boolean =
        password == preferences.getString(PASSWORD_KEY, "1234")

    override fun save(password: String) {
        preferences.edit().putString(PASSWORD_KEY, password).apply()
    }

    private companion object {
        const val PASSWORD_KEY = "password"
    }
}
