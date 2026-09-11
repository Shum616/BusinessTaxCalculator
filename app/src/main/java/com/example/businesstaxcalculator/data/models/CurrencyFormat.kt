package com.example.businesstaxcalculator.data.models

import com.example.businesstaxcalculator.domain.money.ExchangeRate

data class CurrencyFormat(
    val date: String,
    val currency: String,
    val purchaseRate: ExchangeRate,
    val saleRate: ExchangeRate
)
