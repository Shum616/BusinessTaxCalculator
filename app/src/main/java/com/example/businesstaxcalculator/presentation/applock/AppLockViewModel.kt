package com.example.businesstaxcalculator.presentation.applock

import com.example.businesstaxcalculator.domain.settings.AppSettings
import com.example.businesstaxcalculator.domain.security.AppLockCredentials
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AppLockUiState(
    val password: String = "",
    val passwordVisible: Boolean = false,
    val hasPasswordError: Boolean = false
)

@HiltViewModel
class AppLockViewModel @Inject constructor(
    private val settings: AppSettings,
    private val credentials: AppLockCredentials
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppLockUiState())
    val uiState = _uiState.asStateFlow()

    val fingerprintEnabled: Boolean
        get() = settings.fingerprintEnabled

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, hasPasswordError = false) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun validatePassword(): Boolean {
        val valid = credentials.verify(_uiState.value.password)
        _uiState.update { it.copy(hasPasswordError = !valid) }
        return valid
    }
}
