package com.example.businesstaxcalculator.validatot

import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.validatot.base.BaseValidatorInput
import com.example.businesstaxcalculator.validatot.base.ValidateResult

class EmptyValidatorInput(val input: String) : BaseValidatorInput() {
    override fun validate(): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_empty_field
        )
    }
}