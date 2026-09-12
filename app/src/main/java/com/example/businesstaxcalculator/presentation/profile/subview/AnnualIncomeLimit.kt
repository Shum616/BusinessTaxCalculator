package com.example.businesstaxcalculator.presentation.profile.subview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.utils.formatWholeHryvnias

@Composable
fun AnnualIncomeLimit(group: FopGroup) {
    InfoCard(
        stringResource(R.string.annual_income_limit_2026)
    ) {
        Text(
            stringResource(
                R.string.annual_limit_details,
                group.annualIncomeLimitUah.toLong().formatWholeHryvnias(),
                group.minimumSalaryCount
            ),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
