package com.example.businesstaxcalculator.presentation.profile.subview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.money.Money
import com.example.businesstaxcalculator.utils.formatAmount
import com.example.businesstaxcalculator.utils.FopGroup
import com.example.businesstaxcalculator.utils.GROUP_1_MILITARY_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_1_SINGLE_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_2_MILITARY_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_2_SINGLE_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_3_MILITARY_TAX_PERCENT
import com.example.businesstaxcalculator.utils.Group3TaxRate
import com.example.businesstaxcalculator.utils.MONTHLY_ESV_KOPIYKY

@Composable
fun Taxes(group: FopGroup, group3Rate: Group3TaxRate, onGroup3RateChange: (Group3TaxRate) -> Unit) {
    val title =
        if (group == FopGroup.THIRD) R.string.group_3_taxes
        else R.string.monthly_taxes
    InfoCard(
        stringResource(title))
    {
        when (group) {
            FopGroup.FIRST -> {
                TaxLine(
                    R.string.single_tax,
                    stringResource(R.string.amount_uah,
                        Money(GROUP_1_SINGLE_TAX_MONTHLY_KOPIYKY).formatAmount())
                )

                Text(stringResource(
                    R.string.group_1_single_tax_basis),
                    style = MaterialTheme.typography.bodySmall
                )

                TaxLine(
                    R.string.military_tax,
                    stringResource(R.string.amount_uah,
                        Money(GROUP_1_MILITARY_TAX_MONTHLY_KOPIYKY).formatAmount())

                )

                Text(
                    stringResource(R.string.military_tax_basis),
                    style = MaterialTheme.typography.bodySmall
                )

                TaxLine(
                    R.string.esv_for_self,
                    stringResource(R.string.amount_uah,
                        Money(MONTHLY_ESV_KOPIYKY).formatAmount())
                )
            }

            FopGroup.SECOND -> {

                TaxLine(
                    R.string.single_tax,
                    stringResource(R.string.amount_uah,
                        Money(GROUP_2_SINGLE_TAX_MONTHLY_KOPIYKY).formatAmount())
                )

                Text(
                    stringResource(R.string.group_2_single_tax_basis),
                    style = MaterialTheme.typography.bodySmall
                )

                TaxLine(
                    R.string.military_tax,
                    stringResource(R.string.amount_uah,
                        Money(GROUP_2_MILITARY_TAX_MONTHLY_KOPIYKY).formatAmount())
                )

                TaxLine(
                    R.string.esv_for_self,
                    stringResource(R.string.amount_uah,
                        Money(MONTHLY_ESV_KOPIYKY).formatAmount())
                )
            }

            FopGroup.THIRD -> {

                Text(
                    stringResource(R.string.single_tax),
                    style = MaterialTheme.typography.titleMedium)

                Group3RateSelector(
                    group3Rate,
                    onGroup3RateChange)

                TaxLine(
                    R.string.military_tax,
                    stringResource(R.string.percent_of_income_quarterly,
                    GROUP_3_MILITARY_TAX_PERCENT)
                )
                TaxLine(
                    R.string.esv_for_self,
                    stringResource(R.string.amount_uah_monthly,
                        Money(MONTHLY_ESV_KOPIYKY).formatAmount())
                )
            }
        }
    }
}
