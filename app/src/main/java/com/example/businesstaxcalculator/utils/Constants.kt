package com.example.businesstaxcalculator.utils

const val BASE_URL = "https://bank.gov.ua/"
const val FOP_GROUP_PREFERENCE = "fop_group"
const val FOP_GROUP_3_RATE_PREFERENCE = "fop_group_3_rate"

const val GROUP_1_ANNUAL_INCOME_LIMIT_UAH = 1_444_049
const val GROUP_1_SINGLE_TAX_MONTHLY_KOPIYKY = 33_280L
const val GROUP_1_MILITARY_TAX_MONTHLY_KOPIYKY = 86_470L
const val GROUP_2_ANNUAL_INCOME_LIMIT_UAH = 7_211_598
const val GROUP_2_SINGLE_TAX_MONTHLY_KOPIYKY = 172_940L
const val GROUP_2_MILITARY_TAX_MONTHLY_KOPIYKY = 86_470L
const val GROUP_3_ANNUAL_INCOME_LIMIT_UAH = 10_091_049
const val GROUP_3_SINGLE_TAX_WITH_VAT_PERCENT = 3
const val GROUP_3_SINGLE_TAX_WITHOUT_VAT_PERCENT = 5
const val GROUP_3_MILITARY_TAX_PERCENT = 1
const val MONTHLY_ESV_KOPIYKY = 190_234L

enum class FopGroup(val number: Int, val annualIncomeLimitUah: Int, val minimumSalaryCount: Int) {
    FIRST(1, GROUP_1_ANNUAL_INCOME_LIMIT_UAH, 167),
    SECOND(2, GROUP_2_ANNUAL_INCOME_LIMIT_UAH, 834),
    THIRD(3, GROUP_3_ANNUAL_INCOME_LIMIT_UAH, 1_167);

    companion object {
        fun from(number: Int): FopGroup = entries.firstOrNull { it.number == number } ?: FIRST
    }
}

enum class Group3TaxRate(val percent: Int) {
    WITH_VAT(GROUP_3_SINGLE_TAX_WITH_VAT_PERCENT),
    WITHOUT_VAT(GROUP_3_SINGLE_TAX_WITHOUT_VAT_PERCENT);

    companion object {
        fun from(percent: Int): Group3TaxRate = entries.firstOrNull { it.percent == percent } ?: WITHOUT_VAT
    }
}
