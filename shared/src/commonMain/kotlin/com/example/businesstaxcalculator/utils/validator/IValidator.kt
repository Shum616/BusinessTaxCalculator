package com.example.businesstaxcalculator.utils.validator

interface IValidator {
    fun validateInput(income: String): ValidateResult
    fun validateEmpty(input: String): ValidateResult
}