package com.example.businesstaxcalculator.domain.history

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.domain.money.Money

data class IncomeHistoryRecord(
    val date: LocalDate,
    val fopGroup: FopGroup,
    val grossIncome: Money,
    val netProfit: Money,
    val esv: Money,
    val militaryTax: Money,
    val singleTax: Money
)

interface IncomeHistoryRepository {
    fun observeAll(): Flow<List<IncomeHistoryRecord>>
    suspend fun save(record: IncomeHistoryRecord)
    suspend fun deleteAll()
}
