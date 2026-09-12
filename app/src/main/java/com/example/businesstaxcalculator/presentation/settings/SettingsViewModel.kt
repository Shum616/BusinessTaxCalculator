package com.example.businesstaxcalculator.presentation.settings

import com.example.businesstaxcalculator.domain.settings.AppSettings
import com.example.businesstaxcalculator.domain.security.AppLockCredentials
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.data.database.IDataStorage
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import com.example.businesstaxcalculator.domain.money.ExchangeRate
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
    val currentDollarRate: ExchangeRate? = null,
    val currentEuroRate: ExchangeRate? = null,
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
    private val settings: AppSettings,
    private val credentials: AppLockCredentials,
    private val historyRepository: IncomeHistoryRepository,
    private val currencyRateRepository: ICurrencyRateRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            currency = settings.currency,
            dollarRate = settings.dollarRate?.toPlainString().orEmpty(),
            euroRate = settings.euroRate?.toPlainString().orEmpty(),
            appLockEnabled = settings.appLockEnabled,
            fingerprintEnabled = settings.fingerprintEnabled
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
        val dollarRate = ExchangeRate.parse(state.dollarRate)?.takeIf { it.scaledValue > 0 }
        val euroRate = ExchangeRate.parse(state.euroRate)?.takeIf { it.scaledValue > 0 }
        val dollarValid = dollarRate != null
        val euroValid = euroRate != null
        _uiState.update {
            it.copy(hasDollarRateError = !dollarValid, hasEuroRateError = !euroValid)
        }
        if (!dollarValid || !euroValid) return false

        viewModelScope.launch {
            dataStorage.save(
                UserSelection(state.currency, dollarRate, euroRate)
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

        credentials.save(state.password)
        _uiState.update { it.copy(password = "", passwordConfirmation = "") }
        return true
    }

    fun setAppLockEnabled(enabled: Boolean) {
        settings.appLockEnabled = enabled
        _uiState.update {
            it.copy(
                appLockEnabled = enabled,
                fingerprintEnabled = if (enabled) it.fingerprintEnabled else false
            )
        }
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        settings.fingerprintEnabled = enabled
        _uiState.update { it.copy(fingerprintEnabled = enabled) }
    }

    fun deleteIncomeHistory() {
        viewModelScope.launch { historyRepository.deleteAll() }
    }

}
