package com.example.businesstaxcalculator.presentation.history.subview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.domain.history.HistoryPeriod
import com.example.businesstaxcalculator.domain.history.IncomeHistorySummary
import com.example.businesstaxcalculator.utils.periodLabel

@Composable
fun HistoryCard(summary: IncomeHistorySummary, period: HistoryPeriod) {
    Card(
        Modifier
            .fillMaxWidth())
    {
        Column(
            Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                summary.periodLabel(period),
                style = MaterialTheme.typography.titleLarge
            )

            AmountRow(
                Res.string.history_income,
                summary.grossIncome
            )

            AmountRow(
                Res.string.net_gross,
                summary.netProfit
            )

            AmountRow(
                Res.string.history_total_tax,
                summary.totalTax
            )

            AmountRow(
                Res.string.esv_tax,
                summary.esv
            )

            AmountRow(
                Res.string.military_tax,
                summary.militaryTax
            )

            AmountRow(
                Res.string.single_tax,
                summary.singleTax
            )
        }
    }
}