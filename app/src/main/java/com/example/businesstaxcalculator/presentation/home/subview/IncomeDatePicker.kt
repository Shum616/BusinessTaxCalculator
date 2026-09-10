package com.example.businesstaxcalculator.presentation.home.subview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.businesstaxcalculator.R
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val DateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneOffset.UTC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IncomeDatePicker(selectedDateEpochDay: Long, onDateSelected: (Long) -> Unit) {
    var isOpen by rememberSaveable { mutableStateOf(false) }
    val selectedDateMillis = selectedDateEpochDay * MILLIS_PER_DAY
    val dateLabel = remember(selectedDateEpochDay) {
        DateFormat.format(Instant.ofEpochMilli(selectedDateMillis))
    }

    OutlinedButton(onClick = { isOpen = true }, modifier = Modifier.fillMaxWidth()) {
        Text(dateLabel)
    }

    if (isOpen) {

        val state = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

        DatePickerDialog(
            onDismissRequest = { isOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onDateSelected(it / MILLIS_PER_DAY) }
                    isOpen = false
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { isOpen = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

private const val MILLIS_PER_DAY = 86_400_000L
