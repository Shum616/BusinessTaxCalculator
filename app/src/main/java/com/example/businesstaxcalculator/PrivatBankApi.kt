package com.example.businesstaxcalculator

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PrivatBankApi {

    @GET("exchange_rates?json")
    suspend fun getExchangeRates(
        @retrofit2.http.Query("date") date: String // Формат: DD.MM.YYYY
    ): ExchangeRatesResponse
}



object RetrofitInstance {
    private const val BASE_URL = "https://api.privatbank.ua/p24api/"

    val api: PrivatBankApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrivatBankApi::class.java)
    }
}
