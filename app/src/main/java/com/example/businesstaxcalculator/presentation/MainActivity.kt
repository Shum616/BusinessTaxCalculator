package com.example.businesstaxcalculator.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import com.example.businesstaxcalculator.App
import com.example.businesstaxcalculator.data.security.AndroidBiometricAuthenticator
import com.example.businesstaxcalculator.shared.SharedApp

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val biometrics = remember { AndroidBiometricAuthenticator(this) }
            SharedApp((application as App).container, biometrics, onExit = { moveTaskToBack(true) })
        }
    }
}
