package com.example.businesstaxcalculator.ui

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

    fun incomeValidation(text: String): ValidateResult = validator.validateInput(text)

    fun dataStorageSave(userSelection: UserSelection) =
        viewModelScope.launch { dataStorage.save(userSelection) }

    suspend fun currencyRateDollar(date: Date): CurrencyFormat = currencyRate.getDollarRate(date)

    suspend fun currencyRateEuro(date: Date): CurrencyFormat = currencyRate.getEuroRate(date)

    fun calculateUnitedTaxUan(gross: Double, exchangeRate: Double): Double {
        return String.format("%.2f", Math.round(gross * exchangeRate * 0.05 * 100) / 100.0).toDouble()
    }

    fun calculateUnidedSocialСontributionUan(gross: Double): Double {
        return String.format("%.2f", gross * 0.22).toDouble()
    }

    fun calculateIncomeCurrency(gross: Double, currRate: Double): Double {
        return gross * currRate
    }

    fun calculateIncomeUan(gross: Double, currRate: Double): Double {
        return gross * currRate
    }

    fun calculateRemaining(gross: Double): Double {
        return String.format("%.2f", gross - calculateUnidedSocialСontributionUan(gross) -
                calculateUnitedTaxUan(gross, 1.0)).toDouble()
    }

    fun calculateIncomeUanQuarter(incomes: List<Double>): Double {
        return incomes.sum()
    }

    fun calculateRemainingQuarter(incomes: List<Double>): Double {
        var sum: Double = 0.0
        incomes.forEach { it -> sum += it * 0.22 }
        return sum
    }

    fun calculateTaxes(gross: Double) : Double{
        return  String.format("%.2f", gross - calculateRemaining(gross)).toDouble()
    }

    fun passwordValidation(text: String): ValidateResult = validator.validateInput(text)

    fun savePasswords(password: String, preferences:SharedPreferences){
        preferences.edit().putString("password", password).apply()
    }

    private val _usdRate = MutableStateFlow<CurrencyFormat?>(null)
    val usdRate: StateFlow<CurrencyFormat?> = _usdRate

    private val _eurRate = MutableStateFlow<CurrencyFormat?>(null)
    val eurRate: StateFlow<CurrencyFormat?> = _eurRate

    fun fetchRates() {
        val timestamp: Long = System.currentTimeMillis()
        val today = Date(timestamp)

        viewModelScope.launch {
            try {
                _usdRate.value = currencyRate.getDollarRate(today)
                _eurRate.value = currencyRate.getEuroRate(today)
            }  catch (e: CurrencyNotFoundException){
                Log.e("ViewModel", "Помилка при отриманні курсів: ${e.message}")
            }
        }
    }
}