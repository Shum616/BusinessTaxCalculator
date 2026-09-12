package com.example.businesstaxcalculator.presentation

import com.example.businesstaxcalculator.domain.settings.AppSettings
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settings: AppSettings
) : ViewModel() {
    fun isAppLockEnabled(): Boolean = settings.appLockEnabled
}
