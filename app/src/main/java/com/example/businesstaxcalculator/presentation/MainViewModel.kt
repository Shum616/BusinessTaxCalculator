package com.example.businesstaxcalculator.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: SharedPreferences
) : ViewModel() {
    fun isAppLockEnabled(): Boolean = preferences.getBoolean("switch_app_lock", true)
}
