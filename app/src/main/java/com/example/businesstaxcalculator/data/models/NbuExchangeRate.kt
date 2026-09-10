package com.example.businesstaxcalculator.data.models

data class NbuExchangeRate(
    val r030: Int,
    val txt: String,
    val rate: Double,
    val cc: String,
    val exchangedate: String,
    val special: String?
)
