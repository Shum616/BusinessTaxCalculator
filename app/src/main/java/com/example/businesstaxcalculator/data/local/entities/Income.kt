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

    @ColumnInfo(name = "income_united_tax_uan")
    val incomeUnitedTaxUan: Double,

    @ColumnInfo(name = "income_united_local_contribution_uan")
    val incomeUnitedLocalContributionUan: Double,

    @ColumnInfo(name = "income_currency")
    val incomeCurrency: Double,

    @ColumnInfo(name = "income_uan")
    val incomeUan: Double,

    @ColumnInfo(name = "income_remaining")
    val incomeRemaining: Double,

    @ColumnInfo(name = "income_uan_quarter")
    val incomeUanQuarter: Double,

    @ColumnInfo(name = "income_remaining_quarter")
    val incomeRemainingQuarter: Double,

    @ColumnInfo(name = "income_gross")
    val gross: Double,

    @ColumnInfo(name = "exchange_rate")
    val exchangeRate: Double,

    @ColumnInfo(name = "rent")
    val rent: Double?,

    @ColumnInfo(name = "extra_expenses")
    val extraExpenses: Double?,
)
