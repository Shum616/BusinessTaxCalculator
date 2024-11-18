package com.example.businesstaxcalculator.viewmodel

import androidx.lifecycle.ViewModel
import com.example.businesstaxcalculator.validatot.IncomeValidatorInput
import com.example.businesstaxcalculator.validatot.base.ValidateResult
import javax.inject.Inject

class SharedIncomeViewModel @Inject constructor() : ViewModel() {

    fun setIncomeTax(income:String): Array<Double> {
        var icomeNum = income.toDouble()

        var taxResult1 = icomeNum*2
        var taxResult2 = icomeNum*3
        var taxResult3 = icomeNum*4
        var taxResult4 = icomeNum*5

        return arrayOf(taxResult1,taxResult2, taxResult3, taxResult4)
    }

    fun incomeValidation(text: String) : ValidateResult = IncomeValidatorInput(text).validate()
}