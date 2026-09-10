package com.example.businesstaxcalculator.data.local

import com.example.businesstaxcalculator.data.local.dao.IncomeDao
import com.example.businesstaxcalculator.data.local.entities.Income
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRecord
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import com.example.businesstaxcalculator.utils.FopGroup
import javax.inject.Inject

class IncomeHistoryRepositoryImpl @Inject constructor(
    private val incomeDao: IncomeDao
) : IncomeHistoryRepository {
    override fun observeAll() = incomeDao.observeAll().map { incomes ->
        incomes.map { it.toHistoryRecord() }
    }

    override suspend fun save(record: IncomeHistoryRecord) {
        incomeDao.insert(record.toEntity())
    }

    override suspend fun deleteAll() = incomeDao.deleteAll()

    private fun IncomeHistoryRecord.toEntity() = Income(
        incomeId = 0,
        incomeValue = grossIncome.toString(),
        incomeYear = date.year,
        incomeQuarter = (date.monthValue - 1) / 3 + 1,
        incomeDateEpochDay = date.toEpochDay(),
        incomeMilitaryTaxUan = militaryTax,
        fopGroup = fopGroup.number,
        incomeUnitedTaxUan = singleTax,
        incomeUnitedLocalContributionUan = esv,
        incomeCurrency = grossIncome,
        incomeUan = grossIncome,
        incomeRemaining = netProfit,
        incomeUanQuarter = 0.0,
        incomeRemainingQuarter = 0.0,
        gross = grossIncome,
        exchangeRate = 1.0,
        rent = null,
        extraExpenses = null
    )

    private fun Income.toHistoryRecord(): IncomeHistoryRecord {
        val storedDate = incomeDateEpochDay.takeIf { it > 0 }?.let(LocalDate::ofEpochDay)
        val fallbackYear = incomeYear.takeIf { it in 1970..9999 } ?: 1970
        val fallbackMonth = ((incomeQuarter.coerceIn(1, 4) - 1) * 3) + 1
        return IncomeHistoryRecord(
            date = storedDate ?: LocalDate.of(fallbackYear, fallbackMonth, 1),
            fopGroup = FopGroup.from(fopGroup),
            grossIncome = gross.takeIf { it != 0.0 } ?: incomeUan,
            netProfit = incomeRemaining,
            esv = incomeUnitedLocalContributionUan,
            militaryTax = incomeMilitaryTaxUan,
            singleTax = incomeUnitedTaxUan
        )
    }
}
