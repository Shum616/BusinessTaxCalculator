package com.example.businesstaxcalculator.presentation

import com.example.businesstaxcalculator.domain.settings.AppSettings
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(settings: AppSettings) : ViewModel() {
    private val _locked = MutableStateFlow(settings.appLockEnabled)
    val locked = _locked.asStateFlow()
    fun unlock() { _locked.value = false }
}
