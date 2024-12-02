package com.example.businesstaxcalculator.data.remote.repositories.interfaces

import com.example.businesstaxcalculator.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class CurrencyRate : ICurrencyRate {

    suspend fun getRateForCurrency(date: String, currency: String): CurrencyFormat {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getExchangeRates(date)
                val rate = response.exchangeRate.find { it.currency == currency }
                if (rate != null) {
                    CurrencyFormat(
                        date = response.date,
                        currency = rate.currency
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } as CurrencyFormat
        }
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    override fun getDollarRate(date: Date): CurrencyFormat = runBlocking{
        val formattedDate = formatDate(date)
        getRateForCurrency(formattedDate, "USD")
    }

    override fun getEuroRate(date: Date): CurrencyFormat = runBlocking {
        val formattedDate = formatDate(date)
        getRateForCurrency(formattedDate, "EUR")
    }
}