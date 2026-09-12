package com.example.businesstaxcalculator.utils

import androidx.compose.runtime.Composable
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.domain.history.HistoryPeriod
import com.example.businesstaxcalculator.domain.history.IncomeHistorySummary
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import com.example.businesstaxcalculator.domain.money.Money
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

val HistoryPeriod.label: StringResource
    get() = when (this) {
        HistoryPeriod.DAY -> Res.string.history_days
        HistoryPeriod.MONTH -> Res.string.history_months
        HistoryPeriod.QUARTER -> Res.string.history_quarters
        HistoryPeriod.YEAR -> Res.string.history_years
    }

@Composable
fun IncomeHistorySummary.periodLabel(period: HistoryPeriod): String = when (period) {
    HistoryPeriod.DAY -> periodStart.formatDate()
    HistoryPeriod.MONTH -> "${stringArrayResource(Res.array.month_names)[periodStart.month.ordinal]} ${periodStart.year}"
    HistoryPeriod.QUARTER -> stringResource(Res.string.history_quarter_label, periodStart.month.ordinal / 3 + 1, periodStart.year)
    HistoryPeriod.YEAR -> periodStart.year.toString()
}

fun LocalDate.formatDate(): String =
    "${day.toString().padStart(2, '0')}.${(month.ordinal + 1).toString().padStart(2, '0')}.${year.toString().padStart(4, '0')}"

fun Money.formatAmount(): String {
    val whole = kopiyky / 100
    val fraction = kotlin.math.abs(kopiyky % 100).toString().padStart(2, '0')
    val sign = if (kopiyky < 0 && whole == 0L) "-" else ""
    return "$sign${whole.formatWholeHryvnias()},$fraction"
}

fun Long.formatWholeHryvnias(): String {
    val digits = toString().removePrefix("-").reversed().chunked(3).joinToString("\u00A0").reversed()
    return if (this < 0) "-$digits" else digits
}

fun ExchangeRate.formatRate(): String {
    val adjustment = when {
        scaledValue % 100 >= 50 -> 1L
        scaledValue % 100 <= -50 -> -1L
        else -> 0L
    }
    return Money(scaledValue / 100 + adjustment).formatAmount()
}
