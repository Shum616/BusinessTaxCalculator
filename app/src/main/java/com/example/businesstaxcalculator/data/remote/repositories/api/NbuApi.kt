package com.example.businesstaxcalculator.data.remote.repositories.api

import com.example.businesstaxcalculator.data.models.NbuExchangeRate
import retrofit2.http.GET
import retrofit2.http.Query

interface NbuApi {
    @GET("NBUStatService/v1/statdirectory/exchange?json")
    suspend fun getCurrentExchangeRate(
        @Query("valcode") currencyCode: String
    ): List<NbuExchangeRate>
}
