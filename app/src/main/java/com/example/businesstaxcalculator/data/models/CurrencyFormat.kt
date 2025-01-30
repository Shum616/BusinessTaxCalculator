package com.example.businesstaxcalculator.data.models

data class CurrencyFormat(
    val date: String,
    val currency: String,
    val purchaseRate: Double,
    val saleRate: Double
)