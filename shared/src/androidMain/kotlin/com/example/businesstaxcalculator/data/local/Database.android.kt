package com.example.businesstaxcalculator.data.local

import android.content.Context
import androidx.room.Room

fun createDatabase(context: Context): AppDatabase {
    val appContext = context.applicationContext
    return buildDatabase(
        Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = appContext.getDatabasePath(DATABASE_NAME).absolutePath,
        )
    )
}
