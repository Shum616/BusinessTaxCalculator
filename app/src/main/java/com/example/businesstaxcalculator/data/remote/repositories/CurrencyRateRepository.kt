package com.example.businesstaxcalculator.data.remote.repositories

import android.util.Log
import com.example.businesstaxcalculator.data.models.CurrencyFormat
import com.example.businesstaxcalculator.data.models.NbuExchangeRate
import com.example.businesstaxcalculator.data.remote.repositories.api.NbuApi
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import javax.inject.Inject

class CurrencyRateRepository @Inject constructor(
    private val currencyApi: NbuApi
) : ICurrencyRateRepository {
    private val cache = mutableMapOf<String, Pair<Long, NbuExchangeRate>>()

    override suspend fun getDollarRate() = getRate(USD)

    override suspend fun getEuroRate() = getRate(EUR)

    private suspend fun getRate(currencyCode: String): CurrencyFormat {
        val now = System.currentTimeMillis()
        val cached = cache[currencyCode]?.takeIf { now - it.first < CACHE_DURATION_MS }?.second
        val rate = cached ?: try {
            currencyApi.getCurrentExchangeRate(currencyCode).firstOrNull()
                ?.also { cache[currencyCode] = now to it }
                ?: throw CurrencyNotFoundException()
        } catch (exception: Exception) {
            Log.e(TAG, "Cannot load $currencyCode rate from NBU", exception)
            throw CurrencyNotFoundException(exception)
        }

        return CurrencyFormat(
            date = rate.exchangedate,
            currency = rate.cc,
            purchaseRate = rate.rate,
            saleRate = rate.rate
        )
    }

    private companion object {
        const val TAG = "CurrencyRateRepository"
        const val USD = "USD"
        const val EUR = "EUR"
        const val CACHE_DURATION_MS = 15 * 60 * 1000L
    }
}

class CurrencyNotFoundException(cause: Throwable? = null) :
    Exception("Cannot fetch currency rate", cause)
