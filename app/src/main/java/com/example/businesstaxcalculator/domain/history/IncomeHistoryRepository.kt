package com.example.businesstaxcalculator.domain.history

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import com.example.businesstaxcalculator.utils.FopGroup

data class IncomeHistoryRecord(
    val date: LocalDate,
    val fopGroup: FopGroup,
    val grossIncome: Double,
    val netProfit: Double,
    val esv: Double,
    val militaryTax: Double,
    val singleTax: Double
)

interface IncomeHistoryRepository {
    fun observeAll(): Flow<List<IncomeHistoryRecord>>
    suspend fun save(record: IncomeHistoryRecord)
    suspend fun deleteAll()
}
