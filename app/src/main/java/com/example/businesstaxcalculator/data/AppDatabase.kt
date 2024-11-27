package com.example.businesstaxcalculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Income::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            createDatabase(context).also {
                it.also { instance = it }
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-name"
            ).addMigrations(MIGRATION_1_2)
                .build()
    }
}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Income ADD COLUMN income_year INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_quarter INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_united_tax_uan REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_united_local_contribution_uan REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_currency REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_uan REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_remaining REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_uan_quarter REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN income_remaining_quarter REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN gross REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN exchange_rate REAL NOT NULL DEFAULT 0.0")
        database.execSQL("ALTER TABLE Income ADD COLUMN rent REAL")
        database.execSQL("ALTER TABLE Income ADD COLUMN extra_expenses REAL")
    }
}
