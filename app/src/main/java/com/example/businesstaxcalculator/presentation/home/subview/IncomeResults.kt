package com.example.businesstaxcalculator.presentation.home.subview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.calculator.IncomeTaxBreakdown

@Composable
internal fun IncomeResults(results: IncomeTaxBreakdown?) {
    val sections = listOf(
        R.string.net_gross to results?.netProfit,
        R.string.esv_tax to results?.esv,
        R.string.military_tax to results?.militaryTax,
        R.string.single_tax to results?.singleTax
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        sections.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.height(104.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { (label, value) ->
                    IncomeResultCard(
                        value = value,
                        label = label,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
