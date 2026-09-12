package com.example.businesstaxcalculator.utils.validator

import com.example.businesstaxcalculator.domain.money.Money

class Validator : IValidator {
    override fun validateEmpty(input: String): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) ValidationMessage.SUCCESS else ValidationMessage.EMPTY_FIELD
        )
    }

    override fun validateInput(income: String): ValidateResult {
        val value = Money.parse(income)
        val isValid = value != null && value > Money.ZERO
        return ValidateResult(
            isValid,
            if (isValid) ValidationMessage.SUCCESS else ValidationMessage.INVALID_INCOME
        )
    }
}
