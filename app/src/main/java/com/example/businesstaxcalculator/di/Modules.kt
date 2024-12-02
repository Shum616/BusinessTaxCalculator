package com.example.businesstaxcalculator.di

import com.example.businesstaxcalculator.data.remote.repositories.interfaces.CurrencyRate
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRate
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
    fun provideCurrencyRate(): ICurrencyRate = CurrencyRate()
}