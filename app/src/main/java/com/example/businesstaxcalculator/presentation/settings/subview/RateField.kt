package com.example.businesstaxcalculator.presentation.settings.subview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.businesstaxcalculator.R

@Composable
fun RateField(value: String, onValueChange: (String) -> Unit, label: Int, error: Boolean) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true, isError = error,
        supportingText = { if (error) Text(stringResource(R.string.enter_value_again)) },
        modifier = Modifier.fillMaxWidth()
    )
}