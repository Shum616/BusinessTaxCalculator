package com.example.businesstaxcalculator.presentation.profile.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.presentation.profile.ProfileViewModel
import com.example.businesstaxcalculator.presentation.profile.subview.AnnualIncomeLimit
import com.example.businesstaxcalculator.presentation.profile.subview.FopGroupSelector
import com.example.businesstaxcalculator.presentation.profile.subview.Taxes

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            stringResource(Res.string.fop_group_title),
            style = MaterialTheme.typography.headlineSmall
        )

        FopGroupSelector(state.group, viewModel::selectGroup)

        AnnualIncomeLimit(state.group)

        Taxes(state.group, state.group3Rate, viewModel::selectGroup3Rate)
    }
}
