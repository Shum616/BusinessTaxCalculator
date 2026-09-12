package com.example.businesstaxcalculator.presentation.settings.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.presentation.settings.SettingsUiEvent
import com.example.businesstaxcalculator.presentation.settings.SettingsViewModel
import com.example.businesstaxcalculator.presentation.settings.subview.LockSwitch
import com.example.businesstaxcalculator.presentation.settings.subview.RateField
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import com.example.businesstaxcalculator.utils.formatRate

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, biometricAvailable: Boolean)
{

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currencies = listOf(stringResource(Res.string.usd), stringResource(Res.string.eur), stringResource(Res.string.uah))
    var expanded by remember { mutableStateOf(false) }
    var showDeleteHistoryDialog by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            val message = when (event) {
                SettingsUiEvent.RATES_SAVED -> Res.string.rates_saved
                SettingsUiEvent.PASSWORD_SAVED -> Res.string.password_changed_successfully
                SettingsUiEvent.PASSWORD_INVALID -> Res.string.error_in_entering_password
                SettingsUiEvent.HISTORY_DELETED -> Res.string.income_history_deleted
                SettingsUiEvent.FAILED -> Res.string.operation_failed
            }
            snackbar.showSnackbar(org.jetbrains.compose.resources.getString(message))
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(Res.string.choose_currency),
                style = MaterialTheme.typography.titleLarge
            )
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()) {
                    Text(
                        state.currency.ifEmpty { stringResource(Res.string.available_currencies) }
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false })
                {
                    currencies.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = { viewModel.selectCurrency(item); expanded = false })
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        stringResource(Res.string.current_nbu_rates),
                        style = MaterialTheme.typography.titleMedium
                    )
                    CurrentRateRow(
                        "USD", state.currentDollarRate, state.currentDollarRateDate,
                        state.hasCurrentDollarRateError
                    )
                    CurrentRateRow(
                        "EUR", state.currentEuroRate, state.currentEuroRateDate,
                        state.hasCurrentEuroRateError
                    )
                    if (state.isLoadingCurrentRates) CircularProgressIndicator()
                    TextButton(
                        onClick = viewModel::refreshCurrentRates,
                        enabled = !state.isLoadingCurrentRates
                    ) {
                        Text(stringResource(Res.string.refresh_rates))
                    }
                }
            }

            HorizontalDivider()

            Text(
                stringResource(Res.string.custom_rate_input),
                style = MaterialTheme.typography.titleLarge
            )

            RateField(
                state.dollarRate,
                viewModel::updateDollarRate,
                Res.string.dollar_rate,
                state.hasDollarRateError)

            RateField(
                state.euroRate,
                viewModel::updateEuroRate,
                Res.string.euro_rate,
                state.hasEuroRateError)

            Button(onClick = viewModel::saveRates, enabled = !state.isBusy) {
                Text(stringResource(Res.string.get_rate)
                )
            }

            HorizontalDivider()

            Text(
                stringResource(Res.string.change_password_option),
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = state.password, onValueChange = viewModel::updatePassword,
                label = { Text(stringResource(Res.string.new_password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.passwordConfirmation,
                onValueChange = viewModel::updatePasswordConfirmation,
                label = { Text(stringResource(Res.string.confirm_password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = viewModel::savePassword, enabled = !state.isBusy) {
                Text(
                    stringResource(Res.string.save_password)
                )
            }

            HorizontalDivider()

            Text(
                stringResource(Res.string.app_lock_options),
                style = MaterialTheme.typography.titleLarge
            )

            LockSwitch(
                stringResource(Res.string.applock),
                state.appLockEnabled,
                onCheckedChange = viewModel::setAppLockEnabled
            )
            if (biometricAvailable) {
                LockSwitch(
                    stringResource(Res.string.unlock_with_fingerprint),
                    state.fingerprintEnabled,
                    enabled = state.appLockEnabled,
                    onCheckedChange = viewModel::setFingerprintEnabled
                )
            }

            HorizontalDivider()

            Text(
                stringResource(Res.string.history_management),
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedButton(
                onClick = { showDeleteHistoryDialog = true }, enabled = !state.isBusy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.delete_income_history))
            }
        }

    SnackbarHost(snackbar, Modifier.align(androidx.compose.ui.Alignment.BottomCenter))
    }

    if (showDeleteHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteHistoryDialog = false },
            title = { Text(stringResource(Res.string.delete_income_history)) },
            text = { Text(stringResource(Res.string.delete_income_history_confirmation)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteIncomeHistory()
                    showDeleteHistoryDialog = false
                }) {
                    Text(stringResource(Res.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteHistoryDialog = false }) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun CurrentRateRow(currency: String, rate: ExchangeRate?, date: String, hasError: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(currency)
            Text(
                rate?.let { stringResource(Res.string.current_rate_value, it.formatRate()) } ?: "—",
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (date.isNotEmpty()) {
            Text(stringResource(Res.string.current_rates_date, date), style = MaterialTheme.typography.bodySmall)
        }
        if (hasError) {
            Text(
                stringResource(Res.string.current_rates_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
