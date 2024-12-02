package com.example.businesstaxcalculator.data.remote.repositories.interfaces

import java.sql.Date

interface ICurrencyRate {
    fun getDollarRate (date: Date) : CurrencyFormat
    fun getEuroRate (date: Date)  : CurrencyFormat
}