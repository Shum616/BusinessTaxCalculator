package com.example.businesstaxcalculator.data.remote.repositories.interfaces

import com.example.businesstaxcalculator.data.models.CurrencyFormat

interface ICurrencyRateRepository {
    suspend fun getDollarRate(): CurrencyFormat
    suspend fun getEuroRate(): CurrencyFormat
}
