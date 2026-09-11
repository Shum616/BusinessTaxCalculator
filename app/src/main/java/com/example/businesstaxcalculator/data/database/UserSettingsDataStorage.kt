package com.example.businesstaxcalculator.data.database

import android.content.SharedPreferences
import com.example.businesstaxcalculator.data.UserSelection
import javax.inject.Inject
import androidx.core.content.edit
import com.example.businesstaxcalculator.domain.money.ExchangeRate

class UserSettingsDataStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : IDataStorage<UserSelection> {

    private val keySpinner = "spinner_selection"
    private val keyDollarInput = "dollar_input"
    private val keyEuroInput = "euro_input"

    override suspend fun save(data: UserSelection) {
        sharedPreferences.edit {
            putString(keySpinner, data.spinnerSelection)
                .putLong(keyDollarInput, data.dollarInput.scaledValue)
                .putLong(keyEuroInput, data.euroInput.scaledValue)
        }
    }

    override suspend fun load(): UserSelection? {
        val spinnerSelection = sharedPreferences.getString(keySpinner, null) ?: return null
        val dollarInput = sharedPreferences.readRate(keyDollarInput) ?: return null
        val euroInput = sharedPreferences.readRate(keyEuroInput) ?: return null

        return UserSelection(spinnerSelection, dollarInput, euroInput)
    }

    override suspend fun update(data: UserSelection) {
        save(data)
    }

    override suspend fun delete() {
        sharedPreferences.edit {
            remove(keySpinner)
                .remove(keyDollarInput)
                .remove(keyEuroInput)
        }
    }

    override suspend fun hasData(): Boolean {
        return sharedPreferences.contains(keySpinner)
    }

    private fun SharedPreferences.readRate(key: String): ExchangeRate? = when (val value = all[key]) {
        is Long -> ExchangeRate(value)
        is Number -> ExchangeRate.parse(value.toString())
        else -> null
    }
}
