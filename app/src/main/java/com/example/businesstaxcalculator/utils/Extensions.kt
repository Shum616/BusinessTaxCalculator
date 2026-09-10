package com.example.businesstaxcalculator.utils

import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.history.HistoryPeriod
import com.example.businesstaxcalculator.domain.history.IncomeHistorySummary
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

val HistoryPeriod.label: Int
    get() = when (this) {
        HistoryPeriod.DAY -> R.string.history_days
        HistoryPeriod.MONTH -> R.string.history_months
        HistoryPeriod.QUARTER -> R.string.history_quarters
        HistoryPeriod.YEAR -> R.string.history_years
    }

fun IncomeHistorySummary.periodLabel(period: HistoryPeriod): String = when (period) {
    HistoryPeriod.DAY -> periodStart.format(
        DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    HistoryPeriod.MONTH -> periodStart.format(
        DateTimeFormatter.ofPattern("LLLL yyyy", Locale.forLanguageTag("uk-UA"))
    ).replaceFirstChar { it.titlecase(Locale.forLanguageTag("uk-UA")) }
    HistoryPeriod.QUARTER -> "${(periodStart.monthValue - 1) / 3 + 1} квартал ${periodStart.year}"
    HistoryPeriod.YEAR -> periodStart.year.toString()
}

fun Double.formatAmount(): String = NumberFormat
    .getNumberInstance(Locale.forLanguageTag("uk-UA"))
    .apply { minimumFractionDigits = 2; maximumFractionDigits = 2 }
    .format(this)