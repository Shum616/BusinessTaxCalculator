package com.example.businesstaxcalculator.data.remote.repositories

import com.example.businesstaxcalculator.data.remote.repositories.api.PrivatBankApi
import com.example.businesstaxcalculator.data.models.CurrencyFormat
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CurrencyRateRepository @Inject constructor(private val currencyApi: PrivatBankApi) :
    ICurrencyRateRepository {
    private suspend fun getRateForCurrency(date: String, currency: String): CurrencyFormat {
        return withContext(Dispatchers.IO) {
            try {
                val response = currencyApi.getExchangeRates(date)
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

    override fun getDollarRate(date: Date): CurrencyFormat = runBlocking {
        val formattedDate = formatDate(date)
        getRateForCurrency(formattedDate, "USD")
    }

    override fun getEuroRate(date: Date): CurrencyFormat = runBlocking {
        val formattedDate = formatDate(date)
        getRateForCurrency(formattedDate, "EUR")
    }
}