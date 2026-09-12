package com.example.businesstaxcalculator.data.remote.repositories.api

import com.example.businesstaxcalculator.data.models.NbuExchangeRate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class NbuApi(private val client: HttpClient) : AutoCloseable {
    suspend fun getCurrentExchangeRate(currencyCode: String): List<NbuExchangeRate> =
        client.get("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json") {
            parameter("valcode", currencyCode)
        }.body()

    override fun close() = client.close()
}

internal fun createNbuApi(): NbuApi = NbuApi(
    HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 20_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 20_000
        }
    }
)
