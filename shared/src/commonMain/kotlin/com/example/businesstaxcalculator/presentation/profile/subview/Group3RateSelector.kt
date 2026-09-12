package com.example.businesstaxcalculator.presentation.profile.subview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.domain.fop.GROUP_3_SINGLE_TAX_WITHOUT_VAT_PERCENT
import com.example.businesstaxcalculator.domain.fop.GROUP_3_SINGLE_TAX_WITH_VAT_PERCENT
import com.example.businesstaxcalculator.domain.fop.Group3TaxRate

@Composable
fun Group3RateSelector(selected: Group3TaxRate, onSelect: (Group3TaxRate) -> Unit) {
    Column {
        SelectionRow(
            stringResource(Res.string.group_3_rate_without_vat,
                GROUP_3_SINGLE_TAX_WITHOUT_VAT_PERCENT
            ),
            selected == Group3TaxRate.WITHOUT_VAT
        ) { onSelect(Group3TaxRate.WITHOUT_VAT) }
        SelectionRow(
            stringResource(Res.string.group_3_rate_with_vat, GROUP_3_SINGLE_TAX_WITH_VAT_PERCENT),
            selected == Group3TaxRate.WITH_VAT
        ) { onSelect(Group3TaxRate.WITH_VAT) }
    }
}