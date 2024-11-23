package com.example.businesstaxcalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Income::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
}
