package com.example.businesstaxcalculator.utils.validator

data class ValidateResult(
    val isSuccess: Boolean,
    val message: ValidationMessage
)

enum class ValidationMessage { SUCCESS, EMPTY_FIELD, INVALID_INCOME }
