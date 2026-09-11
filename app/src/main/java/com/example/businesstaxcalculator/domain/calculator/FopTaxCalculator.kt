package com.example.businesstaxcalculator.domain.calculator

import com.example.businesstaxcalculator.domain.money.Money
import com.example.businesstaxcalculator.utils.FopGroup
import com.example.businesstaxcalculator.utils.GROUP_1_MILITARY_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_1_SINGLE_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_2_MILITARY_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_2_SINGLE_TAX_MONTHLY_KOPIYKY
import com.example.businesstaxcalculator.utils.GROUP_3_MILITARY_TAX_PERCENT
import com.example.businesstaxcalculator.utils.Group3TaxRate
import com.example.businesstaxcalculator.utils.MONTHLY_ESV_KOPIYKY

data class IncomeTaxBreakdown(
    val netProfit: Money,
    val esv: Money,
    val militaryTax: Money,
    val singleTax: Money
)

fun calculateFopTaxes(
    income: Money,
    group: FopGroup,
    group3Rate: Group3TaxRate
): IncomeTaxBreakdown {
    val esv = Money(MONTHLY_ESV_KOPIYKY)
    val militaryTax: Money
    val singleTax: Money

    when (group) {
        FopGroup.FIRST -> {
            militaryTax = Money(GROUP_1_MILITARY_TAX_MONTHLY_KOPIYKY)
            singleTax = Money(GROUP_1_SINGLE_TAX_MONTHLY_KOPIYKY)
        }
        FopGroup.SECOND -> {
            militaryTax = Money(GROUP_2_MILITARY_TAX_MONTHLY_KOPIYKY)
            singleTax = Money(GROUP_2_SINGLE_TAX_MONTHLY_KOPIYKY)
        }
        FopGroup.THIRD -> {
            militaryTax = income.percent(GROUP_3_MILITARY_TAX_PERCENT)
            singleTax = income.percent(group3Rate.percent)
        }
    }

    return IncomeTaxBreakdown(
        netProfit = income - esv - militaryTax - singleTax,
        esv = esv,
        militaryTax = militaryTax,
        singleTax = singleTax
    )
}
