package com.example.businesstaxcalculator.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.example.businesstaxcalculator.data.local.createDatabase
import com.example.businesstaxcalculator.data.settings.createAppSettings
import com.example.businesstaxcalculator.data.security.IosBiometricAuthenticator
import com.example.businesstaxcalculator.di.AppContainer
import com.example.businesstaxcalculator.domain.security.AppLockCredentials

// The iOS host supplies its Keychain-backed credentials when creating the root controller.
fun MainViewController(credentials: AppLockCredentials) = ComposeUIViewController {
    val container = androidx.compose.runtime.remember {
        AppContainer(createDatabase(), createAppSettings(), credentials)
    }
    androidx.compose.runtime.DisposableEffect(container) {
        onDispose { container.close() }
    }
    val biometrics = androidx.compose.runtime.remember { IosBiometricAuthenticator() }
    SharedApp(container, biometrics)
}
