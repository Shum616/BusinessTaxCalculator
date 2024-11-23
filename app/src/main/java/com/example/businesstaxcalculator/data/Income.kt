package com.example.businesstaxcalculator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Income (
    @PrimaryKey(autoGenerate = true)
    val incomeId: Int,

    @ColumnInfo(name = "income_value")
    val incomeValue: String?,
)
