package com.example.businesstaxcalculator.data.remote.repositories.api

import com.example.businesstaxcalculator.data.models.ExchangeRatesResponse
import retrofit2.http.GET

interface PrivatBankApi {
    /**
     * Returns info about currencies for the passed date.
     *
     * @param date The string of format :DD.MM.YYYY.
     * @return ExchangeRatesResponse.
     */
    @GET("exchange_rates?")
    suspend fun getExchangeRates(
        @retrofit2.http.Query("date") date: String
    ): ExchangeRatesResponse
}
