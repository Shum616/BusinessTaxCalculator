package com.example.businesstaxcalculator.data.local

import com.example.businesstaxcalculator.data.local.dao.IncomeDao
import com.example.businesstaxcalculator.data.local.entities.Income
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRecord
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import com.example.businesstaxcalculator.utils.FopGroup
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import com.example.businesstaxcalculator.domain.money.Money
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
        incomeValue = grossIncome.kopiyky.toString(),
        incomeYear = date.year,
        incomeQuarter = (date.monthValue - 1) / 3 + 1,
        incomeDateEpochDay = date.toEpochDay(),
        incomeMilitaryTaxKopiyky = militaryTax.kopiyky,
        fopGroup = fopGroup.number,
        incomeUnitedTaxKopiyky = singleTax.kopiyky,
        incomeUnitedLocalContributionKopiyky = esv.kopiyky,
        incomeCurrencyKopiyky = grossIncome.kopiyky,
        incomeUahKopiyky = grossIncome.kopiyky,
        incomeRemainingKopiyky = netProfit.kopiyky,
        incomeUahQuarterKopiyky = 0,
        incomeRemainingQuarterKopiyky = 0,
        grossKopiyky = grossIncome.kopiyky,
        exchangeRateScaled = ExchangeRate.SCALE,
        rentKopiyky = null,
        extraExpensesKopiyky = null
    )

    private fun Income.toHistoryRecord(): IncomeHistoryRecord {
        val storedDate = incomeDateEpochDay.takeIf { it > 0 }?.let(LocalDate::ofEpochDay)
        val fallbackYear = incomeYear.takeIf { it in 1970..9999 } ?: 1970
        val fallbackMonth = ((incomeQuarter.coerceIn(1, 4) - 1) * 3) + 1
        return IncomeHistoryRecord(
            date = storedDate ?: LocalDate.of(fallbackYear, fallbackMonth, 1),
            fopGroup = FopGroup.from(fopGroup),
            grossIncome = Money(grossKopiyky.takeIf { it != 0L } ?: incomeUahKopiyky),
            netProfit = Money(incomeRemainingKopiyky),
            esv = Money(incomeUnitedLocalContributionKopiyky),
            militaryTax = Money(incomeMilitaryTaxKopiyky),
            singleTax = Money(incomeUnitedTaxKopiyky)
        )
    }
}
