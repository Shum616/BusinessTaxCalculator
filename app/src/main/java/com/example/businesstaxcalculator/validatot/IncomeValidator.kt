package com.example.businesstaxcalculator.validatot

import android.text.TextUtils
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.validatot.base.BaseValidator
import com.example.businesstaxcalculator.validatot.base.ValidateResult
import java.math.BigDecimal

class IncomeValidator(val income: String) : BaseValidator(){
    override fun validate(): ValidateResult {
        val isValid =  !TextUtils.isEmpty(income) && income.toBigDecimal() > BigDecimal.ZERO
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_income
        )
    }
}

