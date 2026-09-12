package com.example.businesstaxcalculator.domain.history

import kotlinx.datetime.LocalDate
import com.example.businesstaxcalculator.domain.money.Money

enum class HistoryPeriod { DAY, MONTH, QUARTER, YEAR }

data class IncomeHistorySummary(
    val periodStart: LocalDate,
    val grossIncome: Money,
    val netProfit: Money,
    val esv: Money,
    val militaryTax: Money,
    val singleTax: Money
) {
    val totalTax: Money get() = esv + militaryTax + singleTax
}

fun summarizeIncomeHistory(
    records: List<IncomeHistoryRecord>,
    period: HistoryPeriod
): List<IncomeHistorySummary> = records
    .groupBy { it.date.periodStart(period) }
    .map { (start, entries) ->
        IncomeHistorySummary(
            periodStart = start,
            grossIncome = entries.sumMoney { it.grossIncome },
            netProfit = entries.sumMoney { it.netProfit },
            esv = entries.sumMoney { it.esv },
            militaryTax = entries.sumMoney { it.militaryTax },
            singleTax = entries.sumMoney { it.singleTax }
        )
    }
    .sortedByDescending { it.periodStart }

private fun LocalDate.periodStart(period: HistoryPeriod): LocalDate = when (period) {
    HistoryPeriod.DAY -> this
    HistoryPeriod.MONTH -> LocalDate(year, month, 1)
    HistoryPeriod.QUARTER -> LocalDate(year, ((month.ordinal) / 3) * 3 + 1, 1)
    HistoryPeriod.YEAR -> LocalDate(year, 1, 1)
}

private inline fun <T> Iterable<T>.sumMoney(value: (T) -> Money): Money =
    fold(Money.ZERO) { total, item -> total + value(item) }
