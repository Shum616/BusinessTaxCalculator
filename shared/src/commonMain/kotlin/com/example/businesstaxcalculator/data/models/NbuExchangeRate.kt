package com.example.businesstaxcalculator.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
internal data class NbuExchangeRate(
    val r030: Int,
    val txt: String,
    val rate: JsonPrimitive,
    val cc: String,
    val exchangedate: String,
    val special: String? = null
)
