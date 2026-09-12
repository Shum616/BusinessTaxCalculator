package com.example.businesstaxcalculator.domain.settings

import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.domain.fop.Group3TaxRate
import com.example.businesstaxcalculator.domain.money.ExchangeRate

interface AppSettings {
    var fopGroup: FopGroup
    var group3TaxRate: Group3TaxRate
    val currency: String
    val dollarRate: ExchangeRate?
    val euroRate: ExchangeRate?
    var appLockEnabled: Boolean
    var fingerprintEnabled: Boolean

    fun saveSelection(selection: UserSelection)
    fun loadSelection(): UserSelection?
    fun deleteSelection()
    fun hasSelection(): Boolean
}
