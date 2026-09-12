package com.example.businesstaxcalculator.di

import android.content.Context
import com.example.businesstaxcalculator.data.local.createDatabase
import com.example.businesstaxcalculator.data.settings.createAppSettings
import com.example.businesstaxcalculator.data.security.AndroidAppLockCredentials

fun createAppContainer(context: Context): AppContainer {
    val app = context.applicationContext
    val preferences = app.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    return AppContainer(createDatabase(app), createAppSettings(preferences), AndroidAppLockCredentials(preferences))
}
