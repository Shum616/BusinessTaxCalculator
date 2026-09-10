package com.example.businesstaxcalculator.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.businesstaxcalculator.presentation.applock.AppLockActivity
import com.example.businesstaxcalculator.presentation.common.BusinessTaxApp
import com.example.businesstaxcalculator.presentation.home.HomeViewModel
import com.example.businesstaxcalculator.presentation.history.HistoryViewModel
import com.example.businesstaxcalculator.presentation.profile.ProfileViewModel
import com.example.businesstaxcalculator.presentation.settings.SettingsViewModel
import com.example.businesstaxcalculator.presentation.theme.BusinessTaxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null && mainViewModel.isAppLockEnabled()) {
            startActivity(Intent(this, AppLockActivity::class.java))
        }
        setContent {
            BusinessTaxTheme {
                BusinessTaxApp(homeViewModel, historyViewModel, settingsViewModel, profileViewModel)
            }
        }
    }
}
