package com.example.businesstaxcalculator.presentation.history.subview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.utils.formatAmount

@Composable
fun AmountRow(label: Int, amount: Double) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(
            stringResource(label),
            modifier = Modifier.weight(1f)
        )

        Text(
            stringResource(R.string.amount_uah, amount.formatAmount()),
            style = MaterialTheme.typography.titleMedium
        )
    }
}