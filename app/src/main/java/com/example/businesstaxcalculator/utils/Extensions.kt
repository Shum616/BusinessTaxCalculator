package com.example.businesstaxcalculator.utils

import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.history.HistoryPeriod
import com.example.businesstaxcalculator.domain.history.IncomeHistorySummary
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import com.example.businesstaxcalculator.domain.money.Money

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

fun Money.formatAmount(): String {
    val whole = kopiyky / 100
    val fraction = kotlin.math.abs(kopiyky % 100).toString().padStart(2, '0')
    val sign = if (kopiyky < 0 && whole == 0L) "-" else ""
    return "$sign${amountFormatter.format(whole)},$fraction"
}

fun Long.formatWholeHryvnias(): String = amountFormatter.format(this)

fun ExchangeRate.formatRate(): String {
    val roundedKopiyky = (scaledValue + 50L) / 100L
    return Money(roundedKopiyky).formatAmount()
}

private val amountFormatter = NumberFormat.getIntegerInstance(Locale.forLanguageTag("uk-UA"))
