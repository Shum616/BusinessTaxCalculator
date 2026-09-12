package com.example.businesstaxcalculator.presentation.history.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.history.HistoryPeriod
import com.example.businesstaxcalculator.domain.history.IncomeHistorySummary
import com.example.businesstaxcalculator.presentation.history.HistoryViewModel
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.presentation.history.subview.HistoryCard
import com.example.businesstaxcalculator.utils.label
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FopGroup.entries.forEach { group ->
                FilterChip(
                    selected = state.fopGroup == group,
                    onClick = { viewModel.selectFopGroup(group) },
                    label = { Text(stringResource(R.string.fop_group_number, group.number)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HistoryPeriod.entries.forEach { period ->
                FilterChip(
                    selected = state.period == period,
                    onClick = { viewModel.selectPeriod(period) },
                    label = { Text(stringResource(period.label)) }
                )
            }
        }

        if (!state.isLoading && state.summaries.isEmpty()) {
            Text(
                stringResource(R.string.history_empty),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.summaries, key = { it.periodStart.toString() }) { summary ->
                    HistoryCard(
                        summary,
                        state.period
                    )
                }
            }
        }
    }
}
