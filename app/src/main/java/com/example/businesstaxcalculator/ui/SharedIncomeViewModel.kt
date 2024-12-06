package com.example.businesstaxcalculator.ui

import androidx.lifecycle.ViewModel
import com.example.businesstaxcalculator.data.models.CurrencyFormat
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import com.example.businesstaxcalculator.data.local.AppDatabase
import com.example.businesstaxcalculator.utils.validator.IValidator
import com.example.businesstaxcalculator.utils.validator.ValidateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class SharedIncomeViewModel @Inject constructor(
    private val validator: IValidator,
    private val currencyRate: ICurrencyRateRepository,
    private val db: AppDatabase
) : ViewModel() {

    fun setIncomeTax(income: String): Array<Double> {
        val incomeNum = income.toDouble()

        var taxResult1 = incomeNum * 2
        var taxResult2 = incomeNum * 3
        var taxResult3 = incomeNum * 4
        var taxResult4 = incomeNum * 5

        return arrayOf(taxResult1, taxResult2, taxResult3, taxResult4)
    }

    fun incomeValidation(text: String): ValidateResult = validator.validateInput(text)

    fun currencyRateDollar(date: Date): CurrencyFormat = currencyRate.getDollarRate(date)
    
    fun currencyRateEuro(date: Date): CurrencyFormat = currencyRate.getEuroRate(date)
    
    fun calculateUnitedTaxUan(gross: Double, exchangeRate: Double) : Double {
        return Math.round(gross * exchangeRate * 0.05 * 100) /100.0
    }

    fun calculateUnidedSocialСontributionUan(gross : Double) : Double{
        return gross * 0.22
    }

    fun calculateIncomeCurrency(gross: Double, currRate: Double): Double{
        return gross * currRate
    }

    fun calculateIncomeUan(gross: Double, currRate: Double): Double{
        return gross * currRate
    }

    fun calculateRemaining(gross: Double,) : Double{
        return gross - calculateUnidedSocialСontributionUan(gross)
    }

    fun  calculateIncomeUanQuarter(incomes : List<Double>): Double{
        return incomes.sum()
    }

    fun calculateRemainingQuarter(incomes: List<Double>): Double{
        var sum :Double = 0.0
        incomes.forEach { it -> sum += it * 0.22 }
        return sum
    }
}