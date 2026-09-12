package com.example.businesstaxcalculator.domain.security

interface AppLockCredentials {
    fun verify(password: String): Boolean
    fun save(password: String)
}
