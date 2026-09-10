package com.example.businesstaxcalculator.presentation.applock

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.presentation.applock.screen.AppLockScreen
import com.example.businesstaxcalculator.presentation.theme.BusinessTaxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppLockActivity : FragmentActivity() {
    private val viewModel: AppLockViewModel by viewModels()
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    finish()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .setNegativeButtonText(getString(R.string.enter_password))
            .build()

        val fingerprintEnabled = viewModel.fingerprintEnabled &&
            BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
            BiometricManager.BIOMETRIC_SUCCESS

        this.setContent {
            BusinessTaxTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                AppLockScreen(
                    state = state,
                    fingerprintEnabled = fingerprintEnabled,
                    onPasswordChange = viewModel::updatePassword,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                    onUnlock = { if (viewModel.validatePassword()) finish() },
                    onFingerprint = { biometricPrompt.authenticate(promptInfo) },
                    onBack = { moveTaskToBack(true) }
                )
            }
        }
    }
}
