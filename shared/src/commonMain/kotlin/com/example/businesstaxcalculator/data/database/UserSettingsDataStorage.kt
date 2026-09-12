package com.example.businesstaxcalculator.data.database

import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.domain.settings.AppSettings

class UserSettingsDataStorage(private val settings: AppSettings) : IDataStorage<UserSelection> {
    override suspend fun save(data: UserSelection) = settings.saveSelection(data)
    override suspend fun load(): UserSelection? = settings.loadSelection()
    override suspend fun update(data: UserSelection) = save(data)
    override suspend fun delete() = settings.deleteSelection()
    override suspend fun hasData(): Boolean = settings.hasSelection()
}
