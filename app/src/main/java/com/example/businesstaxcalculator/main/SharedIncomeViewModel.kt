package com.example.businesstaxcalculator.main

import androidx.lifecycle.ViewModel
import com.example.businesstaxcalculator.utils.validator.IValidator
import com.example.businesstaxcalculator.utils.validator.ValidateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedIncomeViewModel @Inject constructor(private val validator: IValidator) : ViewModel() {

    fun setIncomeTax(income: String): Array<Double> {
        val incomeNum = income.toDouble()

        var taxResult1 = incomeNum * 2
        var taxResult2 = incomeNum * 3
        var taxResult3 = incomeNum * 4
        var taxResult4 = incomeNum * 5

        return arrayOf(taxResult1, taxResult2, taxResult3, taxResult4)
    }

    fun incomeValidation(text: String): ValidateResult = validator.validateInput(text)
}