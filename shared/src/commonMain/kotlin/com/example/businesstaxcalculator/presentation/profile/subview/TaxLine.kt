package com.example.businesstaxcalculator.presentation.profile.subview

import org.jetbrains.compose.resources.StringResource

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaxLine(label: StringResource, value: String) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(
            stringResource(label),
            modifier = Modifier
                .weight(1f)
        )

        Text(
            value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}