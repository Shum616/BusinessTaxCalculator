package com.example.businesstaxcalculator.validatot

import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.validatot.base.BaseValidator
import com.example.businesstaxcalculator.validatot.base.ValidateResult

class EmptyValidator(val input: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_empty_field
        )
    }
}