package com.example.businesstaxcalculator.di

import com.example.businesstaxcalculator.data.remote.repositories.CurrencyRateRepository
import com.example.businesstaxcalculator.data.remote.repositories.api.PrivatBankApi
import com.example.businesstaxcalculator.data.remote.repositories.interfaces.ICurrencyRateRepository
import com.example.businesstaxcalculator.utils.BASE_URL
import com.example.businesstaxcalculator.utils.validator.IValidator
import com.example.businesstaxcalculator.utils.validator.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideIncomeValidator(): IValidator = Validator()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesPrivatApi(retrofit: Retrofit): PrivatBankApi =
        retrofit.create(PrivatBankApi::class.java)

    @Provides
    @Singleton
    fun provideCurrencyRate(api: PrivatBankApi): ICurrencyRateRepository =
        CurrencyRateRepository(api)
}
