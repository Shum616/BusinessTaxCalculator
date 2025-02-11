package com.example.businesstaxcalculator.data.remote.repositories.interfaces

import com.example.businesstaxcalculator.data.models.CurrencyFormat
import com.example.businesstaxcalculator.data.models.ExchangeRate
import java.sql.Date

interface ICurrencyRateRepository {
    suspend fun getDollarRate (date: Date) : CurrencyFormat
    suspend fun getEuroRate (date: Date) : CurrencyFormat
}