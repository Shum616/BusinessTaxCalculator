package com.example.businesstaxcalculator.data.local

import com.example.businesstaxcalculator.data.local.dao.IncomeDao
import com.example.businesstaxcalculator.data.local.entities.Income
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRecord
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import com.example.businesstaxcalculator.domain.money.Money

class IncomeHistoryRepositoryImpl(
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
        incomeQuarter = date.month.ordinal / 3 + 1,
        incomeDateEpochDay = date.toEpochDays(),
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
        return IncomeHistoryRecord(
            date = LocalDate.fromEpochDays(incomeDateEpochDay),
            fopGroup = FopGroup.from(fopGroup),
            grossIncome = Money(grossKopiyky),
            netProfit = Money(incomeRemainingKopiyky),
            esv = Money(incomeUnitedLocalContributionKopiyky),
            militaryTax = Money(incomeMilitaryTaxKopiyky),
            singleTax = Money(incomeUnitedTaxKopiyky)
        )
    }
}
