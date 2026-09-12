package com.example.businesstaxcalculator.di

import com.example.businesstaxcalculator.data.database.UserSettingsDataStorage
import com.example.businesstaxcalculator.data.local.AppDatabase
import com.example.businesstaxcalculator.data.local.IncomeHistoryRepositoryImpl
import com.example.businesstaxcalculator.data.remote.repositories.createCurrencyRateRepository
import com.example.businesstaxcalculator.domain.security.AppLockCredentials
import com.example.businesstaxcalculator.domain.settings.AppSettings
import com.example.businesstaxcalculator.utils.validator.Validator

class AppContainer(
    private val database: AppDatabase,
    val settings: AppSettings,
    val credentials: AppLockCredentials
) : AutoCloseable {
    val validator = Validator()
    val dataStorage = UserSettingsDataStorage(settings)
    val historyRepository = IncomeHistoryRepositoryImpl(database.incomeDao())
    val currencyRateRepository = createCurrencyRateRepository()

    override fun close() {
        currencyRateRepository.close()
        database.close()
    }
}
