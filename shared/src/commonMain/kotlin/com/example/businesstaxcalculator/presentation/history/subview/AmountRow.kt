package com.example.businesstaxcalculator.presentation.history.subview

import org.jetbrains.compose.resources.StringResource

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.utils.formatAmount
import com.example.businesstaxcalculator.domain.money.Money

@Composable
fun AmountRow(label: StringResource, amount: Money) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(
            stringResource(label),
            modifier = Modifier.weight(1f)
        )

        Text(
            stringResource(Res.string.amount_uah, amount.formatAmount()),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
