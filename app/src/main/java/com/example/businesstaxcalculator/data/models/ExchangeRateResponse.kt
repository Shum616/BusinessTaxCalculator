package com.example.businesstaxcalculator.data.models

data class ExchangeRatesResponse(
    val date: String,
    val bank: String,
    val baseCurrency: Int,
    val baseCurrencyLit: String,
    val exchangeRate: List<ExchangeRate>
)