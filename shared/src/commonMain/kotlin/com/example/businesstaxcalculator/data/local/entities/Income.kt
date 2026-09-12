package com.example.businesstaxcalculator.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Income(
    @PrimaryKey(autoGenerate = true)
    val incomeId: Int,

    @ColumnInfo(name = "income_value")
    val incomeValue: String,

    @ColumnInfo(name = "income_year")
    val incomeYear: Int,

    @ColumnInfo(name = "income_quarter")
    val incomeQuarter: Int,

    @ColumnInfo(name = "income_date_epoch_day")
    val incomeDateEpochDay: Long = 0,

    @ColumnInfo(name = "income_military_tax_uan")
    val incomeMilitaryTaxKopiyky: Long = 0,

    @ColumnInfo(name = "fop_group")
    val fopGroup: Int = 1,

    @ColumnInfo(name = "income_united_tax_uan")
    val incomeUnitedTaxKopiyky: Long,

    @ColumnInfo(name = "income_united_local_contribution_uan")
    val incomeUnitedLocalContributionKopiyky: Long,

    @ColumnInfo(name = "income_currency")
    val incomeCurrencyKopiyky: Long,

    @ColumnInfo(name = "income_uan")
    val incomeUahKopiyky: Long,

    @ColumnInfo(name = "income_remaining")
    val incomeRemainingKopiyky: Long,

    @ColumnInfo(name = "income_uan_quarter")
    val incomeUahQuarterKopiyky: Long,

    @ColumnInfo(name = "income_remaining_quarter")
    val incomeRemainingQuarterKopiyky: Long,

    @ColumnInfo(name = "income_gross")
    val grossKopiyky: Long,

    @ColumnInfo(name = "exchange_rate")
    val exchangeRateScaled: Long,

    @ColumnInfo(name = "rent")
    val rentKopiyky: Long?,

    @ColumnInfo(name = "extra_expenses")
    val extraExpensesKopiyky: Long?,
)
