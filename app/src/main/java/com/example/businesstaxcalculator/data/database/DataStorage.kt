package com.example.businesstaxcalculator.data.database

import android.content.SharedPreferences
import com.example.businesstaxcalculator.data.UserSelection
import javax.inject.Inject

class DataStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences) : IDataStorage<UserSelection>{

        private val keySpinner = "spinner_selection"
    private val keyDollarInput = "dollar_input"
    private val keyEuroInput = "euro_input"

    override suspend fun save(data: UserSelection) {
        sharedPreferences.edit()
            ?.putString(keySpinner, data.spinnerSelection)
            ?.putFloat(keyDollarInput, data.dollarInput.toFloat())
            ?.putFloat(keyEuroInput, data.euroInput.toFloat())
            ?.apply()
    }

    override suspend fun load(): UserSelection? {
        val spinnerSelection = sharedPreferences?.getString(keySpinner, null) ?: return null
        val dollarInput = sharedPreferences.getFloat(keyDollarInput, Float.MIN_VALUE).toDouble()
        val euroInput = sharedPreferences.getFloat(keyEuroInput, Float.MIN_VALUE).toDouble()

        return UserSelection(spinnerSelection, dollarInput, euroInput)
    }

    override suspend fun update(data: UserSelection) {
        save(data)
    }

    override suspend fun delete() {
        sharedPreferences?.edit()
            ?.remove(keySpinner)
            ?.remove(keyDollarInput)
            ?.remove(keyEuroInput)
            ?.apply()
    }

    override suspend fun hasData(): Boolean {
        return sharedPreferences?.contains(keySpinner) == true
    }

}