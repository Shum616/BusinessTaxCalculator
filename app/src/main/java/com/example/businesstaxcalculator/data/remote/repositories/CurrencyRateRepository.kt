package com.example.businesstaxcalculator.data.remote.repositories

import android.util.Log
import com.example.businesstaxcalculator.data.remote.repositories.api.PrivatBankApi
import com.example.businesstaxcalculator.data.models.CurrencyFormat
import com.example.businesstaxcalculator.data.models.ExchangeRate
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CurrencyRateRepository @Inject constructor(private val currencyApi: PrivatBankApi) :
    ICurrencyRateRepository {

    companion object {
        private const val TAG = "CurrencyRateRepository"
        private const val EXPIRE_TIME = 900000L
    }

    private var cashedExchangeRates: Pair<Long, List<ExchangeRate>>? = null

    private suspend fun getRateForCurrency(date: String): List<ExchangeRate> {
        if (cashedExchangeRates == null
            || (System.currentTimeMillis() - (cashedExchangeRates?.first ?: 0)) > EXPIRE_TIME
            || cashedExchangeRates?.second?.isEmpty() != false
        ) cashedExchangeRates = withContext(Dispatchers.IO) {
            try {
                Pair(
                    System.currentTimeMillis(),
                    currencyApi.getExchangeRates(date).exchangeRate
                )
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
                throw CurrencyNotFoundException()
            }
        }
        return cashedExchangeRates?.second ?: throw CurrencyNotFoundException()
    }

    private fun formatDate(date: Date) =
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)

    override suspend fun getDollarRate(date: Date) =
        getRateForCurrency(formatDate(date)).find { it.currency == "USD" }?.let {
            CurrencyFormat(
                date = formatDate(date),
                currency = it.currency,
                purchaseRate = it.purchaseRateNB,
                saleRate = it.saleRateNB
            )
        } ?: throw CurrencyNotFoundException()

    override suspend fun getEuroRate(date: Date) =
        getRateForCurrency(formatDate(date)).find { it.currency == "EUR" }?.let {
            CurrencyFormat(
                date = formatDate(date),
                currency = it.currency,
                purchaseRate = it.purchaseRateNB,
                saleRate = it.saleRateNB
            )
        } ?: throw CurrencyNotFoundException()
}

class CurrencyNotFoundException : Exception("Cant fetch data")
