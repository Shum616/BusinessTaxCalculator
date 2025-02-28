package com.example.businesstaxcalculator.ui.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.data.database.IDataStorage
import com.example.businesstaxcalculator.utils.validator.IValidator
import com.example.businesstaxcalculator.utils.validator.ValidateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.businesstaxcalculator.data.models.CurrencyFormat
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import com.example.businesstaxcalculator.data.local.AppDatabase
import com.example.businesstaxcalculator.data.remote.repositories.CurrencyNotFoundException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class SharedIncomeViewModel @Inject constructor(
    private val validator: IValidator,
    private val dataStorage: IDataStorage<UserSelection>,
    private val currencyRate: ICurrencyRateRepository,
    private val db: AppDatabase
) : ViewModel() {

    private val _usdRate = MutableStateFlow<CurrencyFormat?>(null)
    val usdRate: StateFlow<CurrencyFormat?> = _usdRate

    private val _eurRate = MutableStateFlow<CurrencyFormat?>(null)
    val eurRate: StateFlow<CurrencyFormat?> = _eurRate

    fun incomeValidation(text: String): Double? =
        if (validator.validateInput(text).isSuccess) text.toDouble() else null

    fun dataStorageSave(userSelection: UserSelection) =
        viewModelScope.launch { dataStorage.save(userSelection) }

    suspend fun getRateDollar(date: Date): CurrencyFormat = currencyRate.getDollarRate(date)

    suspend fun getRateEuro(date: Date): CurrencyFormat = currencyRate.getEuroRate(date)

    fun getUnitedTaxUan(gross: Double, exchangeRate: Double) =
        (gross * exchangeRate * 0.05).roundToTwoDecimals()

    fun getUnidedSocialСontributionUan(gross: Double) = (gross * 0.22).roundToTwoDecimals()

    fun getIncomeCurrency(gross: Double, currRate: Double) = gross * currRate

    fun getIncomeUan(gross: Double, currRate: Double) = gross * currRate

    fun getRemaining(gross: Double) =
        (gross - getUnidedSocialСontributionUan(gross)
                - getUnitedTaxUan(gross, 1.0)).roundToTwoDecimals()

    fun getIncomeUanQuarter(incomes: List<Double>) = incomes.sum()

    fun getRemainingQuarter(incomes: List<Double>): Double {
        var sum = 0.0
        incomes.forEach { sum += it * 0.22 }
        return sum
    }

    fun getTaxes(gross: Double) = (gross - getRemaining(gross)).roundToTwoDecimals()

    fun Double.roundToTwoDecimals() = Math.round(this * 100) / 100.0

    fun validatePassword(text: String): ValidateResult = validator.validateInput(text)

    fun savePasswords(password: String, preferences: SharedPreferences) =
        preferences.edit().putString("password", password).apply() //TODO: move this logic to DATA layer

    fun fetchRates() {
        val timestamp: Long = System.currentTimeMillis()
        val today = Date(timestamp)

        viewModelScope.launch {
            try {
                _usdRate.value = currencyRate.getDollarRate(today)
                _eurRate.value = currencyRate.getEuroRate(today)
            } catch (e: CurrencyNotFoundException) {
                Log.e("ViewModel", "Помилка при отриманні курсів: ${e.message}")
            }
        }
    }
}