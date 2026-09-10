package com.example.businesstaxcalculator.presentation.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.data.database.IDataStorage
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import com.example.businesstaxcalculator.utils.validator.IValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val currency: String = "",
    val dollarRate: String = "",
    val euroRate: String = "",
    val hasDollarRateError: Boolean = false,
    val hasEuroRateError: Boolean = false,
    val currentDollarRate: Double? = null,
    val currentEuroRate: Double? = null,
    val currentRatesDate: String = "",
    val isLoadingCurrentRates: Boolean = false,
    val hasCurrentRatesError: Boolean = false,
    val password: String = "",
    val passwordConfirmation: String = "",
    val appLockEnabled: Boolean = true,
    val fingerprintEnabled: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val validator: IValidator,
    private val dataStorage: IDataStorage<UserSelection>,
    private val preferences: SharedPreferences,
    private val historyRepository: IncomeHistoryRepository,
    private val currencyRateRepository: ICurrencyRateRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            currency = preferences.getString(CURRENCY_KEY, "").orEmpty(),
            dollarRate = preferences.rateText(DOLLAR_RATE_KEY),
            euroRate = preferences.rateText(EURO_RATE_KEY),
            appLockEnabled = preferences.getBoolean(APP_LOCK_KEY, true),
            fingerprintEnabled = preferences.getBoolean(FINGERPRINT_KEY, false)
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        refreshCurrentRates()
    }

    fun refreshCurrentRates() {
        if (_uiState.value.isLoadingCurrentRates) return
        _uiState.update { it.copy(isLoadingCurrentRates = true, hasCurrentRatesError = false) }
        viewModelScope.launch {
            runCatching {
                currencyRateRepository.getDollarRate() to currencyRateRepository.getEuroRate()
            }.onSuccess { (dollar, euro) ->
                _uiState.update {
                    it.copy(
                        currentDollarRate = dollar.saleRate,
                        currentEuroRate = euro.saleRate,
                        currentRatesDate = dollar.date,
                        isLoadingCurrentRates = false
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(isLoadingCurrentRates = false, hasCurrentRatesError = true)
                }
            }
        }
    }

    fun selectCurrency(currency: String) = _uiState.update { it.copy(currency = currency) }

    fun updateDollarRate(rate: String) = _uiState.update {
        it.copy(dollarRate = rate, hasDollarRateError = false)
    }

    fun updateEuroRate(rate: String) = _uiState.update {
        it.copy(euroRate = rate, hasEuroRateError = false)
    }

    fun saveRates(): Boolean {
        val state = _uiState.value
        val dollarValid = validator.validateInput(state.dollarRate).isSuccess
        val euroValid = validator.validateInput(state.euroRate).isSuccess
        _uiState.update {
            it.copy(hasDollarRateError = !dollarValid, hasEuroRateError = !euroValid)
        }
        if (!dollarValid || !euroValid) return false

        viewModelScope.launch {
            dataStorage.save(
                UserSelection(state.currency, state.dollarRate.toDouble(), state.euroRate.toDouble())
            )
        }
        return true
    }

    fun updatePassword(password: String) = _uiState.update { it.copy(password = password) }

    fun updatePasswordConfirmation(confirmation: String) =
        _uiState.update { it.copy(passwordConfirmation = confirmation) }

    fun savePassword(): Boolean {
        val state = _uiState.value
        val valid = validator.validateEmpty(state.password).isSuccess &&
            state.password == state.passwordConfirmation
        if (!valid) return false

        preferences.edit { putString(PASSWORD_KEY, state.password) }
        _uiState.update { it.copy(password = "", passwordConfirmation = "") }
        return true
    }

    fun setAppLockEnabled(enabled: Boolean) {
        preferences.edit { putBoolean(APP_LOCK_KEY, enabled) }
        _uiState.update {
            it.copy(
                appLockEnabled = enabled,
                fingerprintEnabled = if (enabled) it.fingerprintEnabled else false
            )
        }
        if (!enabled) preferences.edit { putBoolean(FINGERPRINT_KEY, false) }
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        preferences.edit { putBoolean(FINGERPRINT_KEY, enabled) }
        _uiState.update { it.copy(fingerprintEnabled = enabled) }
    }

    fun deleteIncomeHistory() {
        viewModelScope.launch { historyRepository.deleteAll() }
    }

    private fun SharedPreferences.rateText(key: String): String =
        if (contains(key)) getFloat(key, 0f).toString() else ""

    private companion object {
        const val CURRENCY_KEY = "spinner_selection"
        const val DOLLAR_RATE_KEY = "dollar_input"
        const val EURO_RATE_KEY = "euro_input"
        const val PASSWORD_KEY = "password"
        const val APP_LOCK_KEY = "switch_app_lock"
        const val FINGERPRINT_KEY = "switch_fingerprint_unlock"
    }
}
