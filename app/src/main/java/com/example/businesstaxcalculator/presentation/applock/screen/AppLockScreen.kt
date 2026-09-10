package com.example.businesstaxcalculator.presentation.applock.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.presentation.applock.AppLockUiState

@Composable
fun AppLockScreen(
    state: AppLockUiState,
    fingerprintEnabled: Boolean,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onUnlock: () -> Unit,
    onFingerprint: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .safeDrawingPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.applock),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(R.string.enter_password)) },
                visualTransformation =
                    if (state.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(
                        onClick = onTogglePasswordVisibility) {
                        Text(stringResource(
                            if (state.passwordVisible) R.string.hide_password
                            else R.string.show_password))
                    }
                },
                isError = state.hasPasswordError,
                supportingText = {
                    if (state.hasPasswordError) Text(stringResource(R.string.invalid_password))
                                 },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Button(
                onClick = onUnlock,
                modifier = Modifier
                    .fillMaxWidth())
            {
                Text(stringResource(R.string.unlock))
            }

            if (fingerprintEnabled) {
                OutlinedButton(onClick = onFingerprint) {
                    Text(
                        stringResource(R.string.fingerprint)
                    )
                }
            }
        }
    }
}
