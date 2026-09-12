package com.example.businesstaxcalculator.presentation.applock

import androidx.compose.runtime.Composable

// iOS has no hardware back button. The shared lock screen is the root controller.
@Composable
actual fun PlatformBackHandler(onBack: () -> Unit) = Unit
