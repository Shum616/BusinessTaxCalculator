package com.example.businesstaxcalculator.data

import com.example.businesstaxcalculator.domain.money.ExchangeRate

data class UserSelection(
    var spinnerSelection: String = "",
    var dollarInput: ExchangeRate = ExchangeRate(0),
    var euroInput: ExchangeRate = ExchangeRate(0)
)
