package com.example.businesstaxcalculator.presentation.profile.subview

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

@Composable
fun InfoCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth())
    {
        Column(
            Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp))
        {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge
            )

            content()
        }
    }
}