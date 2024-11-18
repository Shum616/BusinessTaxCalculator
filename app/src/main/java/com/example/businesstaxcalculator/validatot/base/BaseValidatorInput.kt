package com.example.businesstaxcalculator.validatot.base

import com.example.businesstaxcalculator.R
import javax.inject.Inject

abstract class BaseValidatorInput @Inject constructor() : IValidatorInput, IValidatorEmpty{
    companion object {
        fun validate(vararg validators: IValidatorInput): ValidateResult {
            validators.forEach {
                val result = it.validate()
                if (!result.isSuccess)
                    return result
            }
            return ValidateResult(true, R.string.text_validation_success)
        }
    }
}