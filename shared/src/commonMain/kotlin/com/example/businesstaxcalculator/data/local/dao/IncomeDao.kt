package com.example.businesstaxcalculator.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.businesstaxcalculator.data.local.entities.Income
import kotlinx.coroutines.flow.Flow


@Dao
interface IncomeDao {
    @Query("SELECT * FROM income")
    suspend fun getAll(): List<Income>

    @Query("SELECT * FROM income ORDER BY income_date_epoch_day DESC, incomeId DESC")
    fun observeAll(): Flow<List<Income>>

    @Query("DELETE FROM income")
    suspend fun deleteAll()

    @Query("SELECT * FROM income WHERE incomeId IN (:incomeIds)")
    suspend fun loadAllByIds(incomeIds: IntArray): List<Income>

    @Query("SELECT * FROM income WHERE income_year = :year ")
    suspend fun getQuarter(year: Int): List<Income>

    @Insert
    suspend fun insertAll(vararg incomes: Income)

    @Insert
    suspend fun insertOne(vararg income: Income)

    @Delete
    suspend fun delete(income: Income)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: Income)

    @Update
    suspend fun update(income: Income)

}
