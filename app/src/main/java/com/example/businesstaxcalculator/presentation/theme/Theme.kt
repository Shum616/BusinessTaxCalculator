package com.example.businesstaxcalculator.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TaxColors = darkColorScheme(
    primary = Color(0xFFE9B3F2),
    onPrimary = Color(0xFF402E5D),
    primaryContainer = Color(0xFF7750A4),
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF654581),
    onSecondaryContainer = Color.White,
    background = Color(0xFF402E5D),
    onBackground = Color.White,
    surface = Color(0xFF402E5D),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF594568),
    onSurfaceVariant = Color(0xFFF0DCF3)
)

@Composable
fun BusinessTaxTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = TaxColors, content = content)
}
