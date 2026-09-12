package com.example.businesstaxcalculator.data.remote.repositories.interfaces

import com.example.businesstaxcalculator.data.models.CurrencyFormat

interface ICurrencyRateRepository : AutoCloseable {
    suspend fun getDollarRate(forceRefresh: Boolean = false): CurrencyFormat
    suspend fun getEuroRate(forceRefresh: Boolean = false): CurrencyFormat
    override fun close()
}
