package com.example.businesstaxcalculator.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.data.database.IDataStorage
import com.example.businesstaxcalculator.data.database.UserSettingsDataStorage
import com.example.businesstaxcalculator.data.settings.createAppSettings
import com.example.businesstaxcalculator.data.security.AndroidAppLockCredentials
import com.example.businesstaxcalculator.domain.settings.AppSettings
import com.example.businesstaxcalculator.domain.security.AppLockCredentials
import com.example.businesstaxcalculator.data.remote.repositories.createCurrencyRateRepository
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import com.example.businesstaxcalculator.data.local.AppDatabase
import com.example.businesstaxcalculator.data.local.createDatabase
import com.example.businesstaxcalculator.data.local.IncomeHistoryRepositoryImpl
import com.example.businesstaxcalculator.data.local.dao.IncomeDao
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import com.example.businesstaxcalculator.utils.validator.IValidator
import com.example.businesstaxcalculator.utils.validator.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideIncomeValidator(): IValidator = Validator()

    @Provides
    @Singleton
    fun provideCurrencyRate(): ICurrencyRateRepository = createCurrencyRateRepository()
    
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return createDatabase(app.applicationContext)
    }

    @Provides
    fun provideIncomeDao(database: AppDatabase): IncomeDao = database.incomeDao()

    @Provides
    @Singleton
    fun provideIncomeHistoryRepository(
        incomeDao: IncomeDao
    ): IncomeHistoryRepository = IncomeHistoryRepositoryImpl(incomeDao)

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppSettings(sharedPreferences: SharedPreferences): AppSettings =
        createAppSettings(sharedPreferences)

    @Provides
    @Singleton
    fun provideCredentials(sharedPreferences: SharedPreferences): AppLockCredentials =
        AndroidAppLockCredentials(sharedPreferences)

    @Provides
    @Singleton
    fun provideDataStorage(settings: AppSettings): IDataStorage<UserSelection> {
        return UserSettingsDataStorage(settings)
    }
}
