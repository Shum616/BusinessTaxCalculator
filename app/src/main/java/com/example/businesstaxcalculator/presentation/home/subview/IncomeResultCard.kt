package com.example.businesstaxcalculator.presentation.home.subview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.money.Money
import com.example.businesstaxcalculator.utils.formatAmount

@Composable
fun IncomeResultCard(value: Money?, label: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween)
        {
            Text(
                value?.let { stringResource(R.string.amount_uah, it.formatAmount()) } ?: "—",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )

            Text(
                stringResource(label),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
        }
    }
}
