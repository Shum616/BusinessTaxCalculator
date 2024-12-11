package com.example.businesstaxcalculator.data.database

interface IDataStorage<T> {
    suspend fun save(data: T)
    suspend fun load(): T?
    suspend fun update(data: T)
    suspend fun delete()
    suspend fun hasData(): Boolean
}