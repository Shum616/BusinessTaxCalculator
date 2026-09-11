package com.example.businesstaxcalculator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.businesstaxcalculator.data.local.entities.Income
import com.example.businesstaxcalculator.data.local.dao.IncomeDao

@Database(entities = [Income::class], version = 5)
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
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .build()
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Income ADD COLUMN income_date_epoch_day INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_military_tax_uan REAL NOT NULL DEFAULT 0.0")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Income ADD COLUMN fop_group INTEGER NOT NULL DEFAULT 1")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE Income_new (
                incomeId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                income_value TEXT NOT NULL,
                income_year INTEGER NOT NULL,
                income_quarter INTEGER NOT NULL,
                income_date_epoch_day INTEGER NOT NULL,
                income_military_tax_uan INTEGER NOT NULL,
                fop_group INTEGER NOT NULL,
                income_united_tax_uan INTEGER NOT NULL,
                income_united_local_contribution_uan INTEGER NOT NULL,
                income_currency INTEGER NOT NULL,
                income_uan INTEGER NOT NULL,
                income_remaining INTEGER NOT NULL,
                income_uan_quarter INTEGER NOT NULL,
                income_remaining_quarter INTEGER NOT NULL,
                income_gross INTEGER NOT NULL,
                exchange_rate INTEGER NOT NULL,
                rent INTEGER,
                extra_expenses INTEGER
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO Income_new SELECT
                incomeId, income_value, income_year, income_quarter,
                income_date_epoch_day,
                CAST(ROUND(income_military_tax_uan * 100) AS INTEGER),
                fop_group,
                CAST(ROUND(income_united_tax_uan * 100) AS INTEGER),
                CAST(ROUND(income_united_local_contribution_uan * 100) AS INTEGER),
                CAST(ROUND(income_currency * 100) AS INTEGER),
                CAST(ROUND(income_uan * 100) AS INTEGER),
                CAST(ROUND(income_remaining * 100) AS INTEGER),
                CAST(ROUND(income_uan_quarter * 100) AS INTEGER),
                CAST(ROUND(income_remaining_quarter * 100) AS INTEGER),
                CAST(ROUND(income_gross * 100) AS INTEGER),
                CAST(ROUND(exchange_rate * 10000) AS INTEGER),
                CASE WHEN rent IS NULL THEN NULL ELSE CAST(ROUND(rent * 100) AS INTEGER) END,
                CASE WHEN extra_expenses IS NULL THEN NULL ELSE CAST(ROUND(extra_expenses * 100) AS INTEGER) END
            FROM Income
            """.trimIndent()
        )
        db.execSQL("DROP TABLE Income")
        db.execSQL("ALTER TABLE Income_new RENAME TO Income")
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Income ADD COLUMN income_year INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_quarter INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_united_tax_uan REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_united_local_contribution_uan REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_currency REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_uan REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_remaining REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_uan_quarter REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN income_remaining_quarter REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN gross REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN exchange_rate REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE Income ADD COLUMN rent REAL")
        db.execSQL("ALTER TABLE Income ADD COLUMN extra_expenses REAL")
    }
}
