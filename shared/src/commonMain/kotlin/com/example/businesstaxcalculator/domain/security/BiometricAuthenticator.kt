package com.example.businesstaxcalculator.domain.security

interface BiometricAuthenticator {
    val available: Boolean
    suspend fun authenticate(title: String, cancel: String): Boolean
}
