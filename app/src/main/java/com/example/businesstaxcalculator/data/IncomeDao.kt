package com.example.businesstaxcalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface IncomeDao {
    @Query("SELECT * FROM income")
    fun getAll(): List<Income>

    @Query("SELECT * FROM income WHERE incomeId IN (:incomeIds)")
    fun loadAllByIds(incomeIds: IntArray): List<Income>

    @Query("SELECT * FROM income WHERE income_year = :year ")
    fun getQuarter(year: Int) : List<Income>

    @Insert
    fun insertAll(vararg incomes: Income)

    @Insert
    fun insertOne(vararg income: Income)

    @Delete
    fun delete(income: Income)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: Income)

    @Update
    fun update(income: Income)

}
