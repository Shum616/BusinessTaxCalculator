package com.example.businesstaxcalculator.presentation.applock

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(onBack: () -> Unit) = BackHandler(onBack = onBack)
