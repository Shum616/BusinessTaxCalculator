package com.example.businesstaxcalculator.ui

import androidx.lifecycle.ViewModel
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.CurrencyFormat
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRate
import com.example.businesstaxcalculator.utils.validator.IValidator
import com.example.businesstaxcalculator.utils.validator.ValidateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class SharedIncomeViewModel @Inject constructor(private val validator: IValidator, private val currencyRate: ICurrencyRate) : ViewModel() {

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
}