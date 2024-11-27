package com.example.businesstaxcalculator

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PrivatBankApi {
    @GET("exchange_rates?json")
    fun getExchangeRates(
        @Query("date") date: String // Формат DD.MM.YYYY
    ): Call<ExchangeRatesResponse>
}