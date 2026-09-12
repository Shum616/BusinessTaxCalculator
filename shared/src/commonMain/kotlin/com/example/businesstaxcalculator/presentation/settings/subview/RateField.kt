package com.example.businesstaxcalculator.presentation.settings.subview

import org.jetbrains.compose.resources.StringResource

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.businesstaxcalculator.resources.*

@Composable
fun RateField(value: String, onValueChange: (String) -> Unit, label: StringResource, error: Boolean) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true, isError = error,
        supportingText = { if (error) Text(stringResource(Res.string.enter_value_again)) },
        modifier = Modifier.fillMaxWidth()
    )
}