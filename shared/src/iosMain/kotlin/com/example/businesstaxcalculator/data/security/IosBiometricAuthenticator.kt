package com.example.businesstaxcalculator.data.security

import com.example.businesstaxcalculator.domain.security.BiometricAuthenticator
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
class IosBiometricAuthenticator : BiometricAuthenticator {
    override val available: Boolean
        get() = LAContext().canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)

    override suspend fun authenticate(title: String, cancel: String): Boolean = suspendCancellableCoroutine { continuation ->
        val context = LAContext()
        context.localizedCancelTitle = cancel
        context.localizedFallbackTitle = ""
        continuation.invokeOnCancellation { context.invalidate() }
        context.evaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, title) { success, _ ->
            if (continuation.isActive) continuation.resume(success)
        }
    }
}
