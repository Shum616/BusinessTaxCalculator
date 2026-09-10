package com.example.businesstaxcalculator.presentation.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.presentation.home.HomeUiState
import com.example.businesstaxcalculator.presentation.home.HomeUiEvent
import com.example.businesstaxcalculator.presentation.home.HomeViewModel
import com.example.businesstaxcalculator.presentation.home.subview.IncomeDatePicker
import com.example.businesstaxcalculator.presentation.home.subview.IncomeResults

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeUiEvent.HistorySaved -> snackbarHostState.showSnackbar(
                    context.getString(R.string.history_saved, event.fopGroupNumber)
                )
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        HomeScreen(state, viewModel::updateIncome, viewModel::selectDate, viewModel::calculate)
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onIncomeChange: (String) -> Unit,
    onDateSelected: (Long) -> Unit,
    onCalculate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IncomeDatePicker(state.selectedDateEpochDay, onDateSelected)

        IncomeField(
            income = state.income,
            hasError = state.hasIncomeError,
            onIncomeChange = onIncomeChange
        )

        IncomeResults(state.result)

        Button(
            onClick = onCalculate,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.calculate_taxes))
        }
    }
}

@Composable
private fun IncomeField(income: String, hasError: Boolean, onIncomeChange: (String) -> Unit) {
    OutlinedTextField(
        value = income,
        onValueChange = onIncomeChange,
        label = { Text(stringResource(R.string.enter_income)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        isError = hasError,
        supportingText = {
            if (hasError) Text(stringResource(R.string.text_validation_error_income))
        },
        modifier = Modifier.fillMaxWidth()
    )
}
