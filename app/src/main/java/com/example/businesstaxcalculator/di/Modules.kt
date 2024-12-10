package com.example.businesstaxcalculator.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.businesstaxcalculator.data.AppDatabase
import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.data.database.DataStorage
import com.example.businesstaxcalculator.data.database.IDataStorage
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
    fun provideDatabase(app: Application): AppDatabase {
        return AppDatabase.invoke(app.applicationContext)
    }

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
    fun provideDataStorage(sharedPreferences: SharedPreferences): IDataStorage<UserSelection> {
        return DataStorage(sharedPreferences)
    }
}