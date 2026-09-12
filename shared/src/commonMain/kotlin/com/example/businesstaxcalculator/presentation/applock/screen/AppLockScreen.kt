package com.example.businesstaxcalculator.presentation.applock.screen

import com.example.businesstaxcalculator.presentation.applock.PlatformBackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.resources.*
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
    PlatformBackHandler(onBack = onBack)

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
                stringResource(Res.string.applock),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(Res.string.enter_password)) },
                visualTransformation =
                    if (state.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(
                        onClick = onTogglePasswordVisibility) {
                        Text(stringResource(
                            if (state.passwordVisible) Res.string.hide_password
                            else Res.string.show_password))
                    }
                },
                isError = state.hasPasswordError,
                supportingText = {
                    if (state.hasPasswordError) Text(stringResource(Res.string.invalid_password))
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
                Text(stringResource(Res.string.unlock))
            }

            if (fingerprintEnabled) {
                OutlinedButton(onClick = onFingerprint) {
                    Text(
                        stringResource(Res.string.fingerprint)
                    )
                }
            }
        }
    }
}
