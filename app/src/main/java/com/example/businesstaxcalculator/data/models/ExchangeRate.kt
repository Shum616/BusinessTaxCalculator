package com.example.businesstaxcalculator.data.models

data class ExchangeRate(
    val baseCurrency: String,
    val currency: String,
    val saleRateNB: Double,
    val purchaseRateNB: Double,
    val saleRate: Double?,
    val purchaseRate: Double?
)
