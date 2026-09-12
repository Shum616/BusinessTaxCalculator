package com.example.businesstaxcalculator.presentation.profile.subview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.domain.money.Money
import com.example.businesstaxcalculator.utils.formatAmount
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.domain.fop.GROUP_1_MILITARY_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.domain.fop.GROUP_1_SINGLE_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.domain.fop.GROUP_2_MILITARY_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.domain.fop.GROUP_2_SINGLE_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.domain.fop.GROUP_3_MILITARY_TAX_PERCENT
import com.example.businesstaxcalculator.domain.fop.Group3TaxRate
import com.example.businesstaxcalculator.domain.fop.MONTHLY_ESV_KOPIYKY

@Composable
fun Taxes(group: FopGroup, group3Rate: Group3TaxRate, onGroup3RateChange: (Group3TaxRate) -> Unit) {
    val title =
        if (group == FopGroup.THIRD) Res.string.group_3_taxes
        else Res.string.monthly_taxes
    InfoCard(
        stringResource(title))
    {
        when (group) {
            FopGroup.FIRST -> {
                TaxLine(
                    Res.string.single_tax,
                    stringResource(Res.string.amount_uah,
                        Money(GROUP_1_SINGLE_TAX_MONTHLY_KOPIYKY).formatAmount())
                )

                Text(stringResource(
                    Res.string.group_1_single_tax_basis),
                    style = MaterialTheme.typography.bodySmall
                )

                TaxLine(
                    Res.string.military_tax,
                    stringResource(Res.string.amount_uah,
                        Money(GROUP_1_MILITARY_TAX_MONTHLY_KOPIYKY).formatAmount())

                )

                Text(
                    stringResource(Res.string.military_tax_basis),
                    style = MaterialTheme.typography.bodySmall
                )

                TaxLine(
                    Res.string.esv_for_self,
                    stringResource(Res.string.amount_uah,
                        Money(MONTHLY_ESV_KOPIYKY).formatAmount())
                )
            }

            FopGroup.SECOND -> {

                TaxLine(
                    Res.string.single_tax,
                    stringResource(Res.string.amount_uah,
                        Money(GROUP_2_SINGLE_TAX_MONTHLY_KOPIYKY).formatAmount())
                )

                Text(
                    stringResource(Res.string.group_2_single_tax_basis),
                    style = MaterialTheme.typography.bodySmall
                )

                TaxLine(
                    Res.string.military_tax,
                    stringResource(Res.string.amount_uah,
                        Money(GROUP_2_MILITARY_TAX_MONTHLY_KOPIYKY).formatAmount())
                )

                TaxLine(
                    Res.string.esv_for_self,
                    stringResource(Res.string.amount_uah,
                        Money(MONTHLY_ESV_KOPIYKY).formatAmount())
                )
            }

            FopGroup.THIRD -> {

                Text(
                    stringResource(Res.string.single_tax),
                    style = MaterialTheme.typography.titleMedium)

                Group3RateSelector(
                    group3Rate,
                    onGroup3RateChange)

                TaxLine(
                    Res.string.military_tax,
                    stringResource(Res.string.percent_of_income_quarterly,
                    GROUP_3_MILITARY_TAX_PERCENT)
                )
                TaxLine(
                    Res.string.esv_for_self,
                    stringResource(Res.string.amount_uah_monthly,
                        Money(MONTHLY_ESV_KOPIYKY).formatAmount())
                )
            }
        }
    }
}
