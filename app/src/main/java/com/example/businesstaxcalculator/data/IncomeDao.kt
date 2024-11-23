package com.example.businesstaxcalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IncomeDao {
    @Query("SELECT * FROM income")
    fun getAll(): List<Income>

    @Query("SELECT * FROM income WHERE incomeId IN (:incomeIds)")
    fun loadAllByIds(incomeIds: IntArray): List<Income>

    @Insert
    fun insertAll(vararg incomes: Income)

    @Delete
    fun delete(income: Income)
}
