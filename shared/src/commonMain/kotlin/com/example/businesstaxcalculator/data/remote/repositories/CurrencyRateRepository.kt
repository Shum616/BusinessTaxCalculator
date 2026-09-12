package com.example.businesstaxcalculator.data.remote.repositories

import com.example.businesstaxcalculator.data.models.CurrencyFormat
import com.example.businesstaxcalculator.data.remote.repositories.api.NbuApi
import com.example.businesstaxcalculator.data.remote.repositories.api.createNbuApi
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource

fun createCurrencyRateRepository(): ICurrencyRateRepository = CurrencyRateRepository(createNbuApi())

internal class CurrencyRateRepository(private val api: NbuApi) : ICurrencyRateRepository {
    private val dollar = CurrencyCache("USD")
    private val euro = CurrencyCache("EUR")

    override suspend fun getDollarRate(forceRefresh: Boolean) = getRate(dollar, forceRefresh)
    override suspend fun getEuroRate(forceRefresh: Boolean) = getRate(euro, forceRefresh)
    override fun close() = api.close()

    private suspend fun getRate(cache: CurrencyCache, forceRefresh: Boolean): CurrencyFormat =
        cache.mutex.withLock {
            val cached = cache.value
            if (!forceRefresh && cached != null && cache.updatedAt!!.elapsedNow() < 15.minutes) {
                return@withLock cached
            }
            try {
                val response = api.getCurrentExchangeRate(cache.code)
                    .firstOrNull { it.cc == cache.code }
                    ?: throw CurrencyNotFoundException()
                // Read the original JSON decimal token without passing through Double.
                val rate = ExchangeRate.parse(response.rate.content)
                    ?.takeIf { it.scaledValue > 0 }
                    ?: throw CurrencyNotFoundException()
                if (response.exchangedate.isBlank()) throw CurrencyNotFoundException()
                CurrencyFormat(response.exchangedate, response.cc, rate, rate).also {
                    cache.value = it
                    cache.updatedAt = TimeSource.Monotonic.markNow()
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (exception: Exception) {
                throw CurrencyNotFoundException(exception)
            }
        }

    private class CurrencyCache(val code: String) {
        val mutex = Mutex()
        var value: CurrencyFormat? = null
        var updatedAt: TimeMark? = null
    }
}

class CurrencyNotFoundException(cause: Throwable? = null) :
    Exception("Cannot fetch currency rate", cause)
