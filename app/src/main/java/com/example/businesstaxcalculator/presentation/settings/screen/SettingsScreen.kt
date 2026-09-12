package com.example.businesstaxcalculator.presentation.settings.screen

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.presentation.settings.SettingsViewModel
import com.example.businesstaxcalculator.presentation.settings.subview.LockSwitch
import com.example.businesstaxcalculator.presentation.settings.subview.RateField
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import com.example.businesstaxcalculator.utils.formatRate

@Composable
fun SettingsScreen(viewModel: SettingsViewModel)
{
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currencies = listOf(stringResource(R.string.usd), stringResource(R.string.eur), stringResource(R.string.uah))
    var expanded by remember { mutableStateOf(false) }
    var showDeleteHistoryDialog by remember { mutableStateOf(false) }
    val biometricAvailable = remember(context) {
        BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
            BiometricManager.BIOMETRIC_SUCCESS
    }

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(R.string.choose_currency),
            style = MaterialTheme.typography.titleLarge
        )
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    state.currency.ifEmpty { stringResource(R.string.available_currencies) }
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
                    stringResource(R.string.current_nbu_rates),
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
                    Text(stringResource(R.string.refresh_rates))
                }
            }
        }

        HorizontalDivider()

        Text(
            stringResource(R.string.custom_rate_input),
            style = MaterialTheme.typography.titleLarge
        )

        RateField(
            state.dollarRate,
            viewModel::updateDollarRate,
            R.string.dollar_rate,
            state.hasDollarRateError)

        RateField(
            state.euroRate,
            viewModel::updateEuroRate,
            R.string.euro_rate,
            state.hasEuroRateError)

        Button(onClick = {
            if (viewModel.saveRates()) {
                Toast.makeText(context, R.string.rates_saved, Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(stringResource(R.string.get_rate)
            )
        }

        HorizontalDivider()

        Text(
            stringResource(R.string.change_password_option),
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = state.password, onValueChange = viewModel::updatePassword,
            label = { Text(stringResource(R.string.new_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.passwordConfirmation,
            onValueChange = viewModel::updatePasswordConfirmation,
            label = { Text(stringResource(R.string.confirm_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
            val valid = viewModel.savePassword()
            Toast.makeText(context, if (valid) R.string.password_changed_successfully else R.string.error_in_entering_password,
                Toast.LENGTH_SHORT).show()
        }) {
            Text(
                stringResource(R.string.save_password)
            )
        }

        HorizontalDivider()

        Text(
            stringResource(R.string.app_lock_options),
            style = MaterialTheme.typography.titleLarge
        )

        LockSwitch(
            stringResource(R.string.applock),
            state.appLockEnabled,
            onCheckedChange = viewModel::setAppLockEnabled
        )
        if (biometricAvailable) {
            LockSwitch(
                stringResource(R.string.unlock_with_fingerprint),
                state.fingerprintEnabled,
                enabled = state.appLockEnabled,
                onCheckedChange = viewModel::setFingerprintEnabled
            )
        }

        HorizontalDivider()

        Text(
            stringResource(R.string.history_management),
            style = MaterialTheme.typography.titleLarge
        )
        OutlinedButton(
            onClick = { showDeleteHistoryDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete_income_history))
        }
    }

    if (showDeleteHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteHistoryDialog = false },
            title = { Text(stringResource(R.string.delete_income_history)) },
            text = { Text(stringResource(R.string.delete_income_history_confirmation)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteIncomeHistory()
                    showDeleteHistoryDialog = false
                    Toast.makeText(context, R.string.income_history_deleted, Toast.LENGTH_SHORT).show()
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteHistoryDialog = false }) {
                    Text(stringResource(R.string.cancel))
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
                rate?.let { stringResource(R.string.current_rate_value, it.formatRate()) } ?: "—",
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (date.isNotEmpty()) {
            Text(stringResource(R.string.current_rates_date, date), style = MaterialTheme.typography.bodySmall)
        }
        if (hasError) {
            Text(
                stringResource(R.string.current_rates_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
