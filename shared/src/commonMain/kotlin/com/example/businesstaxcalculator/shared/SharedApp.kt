package com.example.businesstaxcalculator.shared

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.businesstaxcalculator.di.AppContainer
import com.example.businesstaxcalculator.domain.security.BiometricAuthenticator
import com.example.businesstaxcalculator.presentation.MainViewModel
import com.example.businesstaxcalculator.presentation.applock.AppLockViewModel
import com.example.businesstaxcalculator.presentation.applock.screen.AppLockScreen
import com.example.businesstaxcalculator.presentation.common.BusinessTaxApp
import com.example.businesstaxcalculator.presentation.theme.BusinessTaxTheme
import com.example.businesstaxcalculator.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun SharedApp(container: AppContainer, biometrics: BiometricAuthenticator, onExit: () -> Unit = {}) {
    val main: MainViewModel = viewModel { MainViewModel(container.settings) }
    val locked by main.locked.collectAsStateWithLifecycle()
    BusinessTaxTheme {
        if (locked) {
            val lock: AppLockViewModel = viewModel { AppLockViewModel(container.settings, container.credentials) }
            val state by lock.uiState.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            var authenticating by remember { mutableStateOf(false) }
            val title = stringResource(Res.string.unlock)
            val cancel = stringResource(Res.string.cancel)
            AppLockScreen(
                state, lock.fingerprintEnabled && biometrics.available,
                lock::updatePassword, lock::togglePasswordVisibility,
                onUnlock = { if (lock.validatePassword()) main.unlock() },
                onFingerprint = {
                    if (!authenticating) {
                        authenticating = true
                        scope.launch {
                            try {
                                if (biometrics.authenticate(title, cancel)) main.unlock()
                            } finally { authenticating = false }
                        }
                    }
                },
                onBack = onExit
            )
        } else {
            BusinessTaxApp(container, biometrics.available)
        }
    }
}
