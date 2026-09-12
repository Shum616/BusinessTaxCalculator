package com.example.businesstaxcalculator.presentation.applock

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformBackHandler(onBack: () -> Unit)
