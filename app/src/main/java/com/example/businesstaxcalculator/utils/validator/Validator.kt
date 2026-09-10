package com.example.businesstaxcalculator.utils.validator

import com.example.businesstaxcalculator.R

class Validator : IValidator {
    override fun validateEmpty(input: String): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_empty_field
        )
    }

    override fun validateInput(income: String): ValidateResult {
        val value = income.toDoubleOrNull()
        val isValid = value != null && value.isFinite() && value > 0.0
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_income
        )
    }
}
