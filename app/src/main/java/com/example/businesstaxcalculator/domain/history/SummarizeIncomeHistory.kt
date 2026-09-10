package com.example.businesstaxcalculator.domain.history

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

enum class HistoryPeriod { DAY, MONTH, QUARTER, YEAR }

data class IncomeHistorySummary(
    val periodStart: LocalDate,
    val grossIncome: Double,
    val netProfit: Double,
    val esv: Double,
    val militaryTax: Double,
    val singleTax: Double
) {
    val totalTax: Double get() = (esv + militaryTax + singleTax).money()
}

fun summarizeIncomeHistory(
    records: List<IncomeHistoryRecord>,
    period: HistoryPeriod
): List<IncomeHistorySummary> = records
    .groupBy { it.date.periodStart(period) }
    .map { (start, entries) ->
        IncomeHistorySummary(
            periodStart = start,
            grossIncome = entries.sumOf { it.grossIncome }.money(),
            netProfit = entries.sumOf { it.netProfit }.money(),
            esv = entries.sumOf { it.esv }.money(),
            militaryTax = entries.sumOf { it.militaryTax }.money(),
            singleTax = entries.sumOf { it.singleTax }.money()
        )
    }
    .sortedByDescending { it.periodStart }

private fun LocalDate.periodStart(period: HistoryPeriod): LocalDate = when (period) {
    HistoryPeriod.DAY -> this
    HistoryPeriod.MONTH -> withDayOfMonth(1)
    HistoryPeriod.QUARTER -> LocalDate.of(year, ((monthValue - 1) / 3) * 3 + 1, 1)
    HistoryPeriod.YEAR -> LocalDate.of(year, 1, 1)
}

private fun Double.money() = BigDecimal.valueOf(this).setScale(2, RoundingMode.HALF_UP).toDouble()
