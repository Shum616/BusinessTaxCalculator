package com.example.businesstaxcalculator.data.settings

import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.domain.fop.Group3TaxRate
import com.example.businesstaxcalculator.domain.money.ExchangeRate
import com.example.businesstaxcalculator.domain.settings.AppSettings

internal class StoredAppSettings(private val store: PreferencesStore) : AppSettings {
    override var fopGroup: FopGroup
        get() = FopGroup.from(store.read(FOP_GROUP)?.toIntOrNull() ?: FopGroup.FIRST.number)
        set(value) = store.write(mapOf(FOP_GROUP to value.number.toString()))

    override var group3TaxRate: Group3TaxRate
        get() = Group3TaxRate.from(
            store.read(GROUP_3_RATE)?.toIntOrNull() ?: Group3TaxRate.WITHOUT_VAT.percent
        )
        set(value) = store.write(mapOf(GROUP_3_RATE to value.percent.toString()))

    override val currency: String get() = store.read(CURRENCY).orEmpty()
    override val dollarRate: ExchangeRate? get() = readRate(DOLLAR_RATE)
    override val euroRate: ExchangeRate? get() = readRate(EURO_RATE)

    override var appLockEnabled: Boolean
        get() = store.read(APP_LOCK)?.toBooleanStrictOrNull() ?: true
        set(value) {
            store.write(
                if (value) mapOf(APP_LOCK to "true")
                else mapOf(APP_LOCK to "false", FINGERPRINT to "false")
            )
        }

    override var fingerprintEnabled: Boolean
        get() = store.read(FINGERPRINT)?.toBooleanStrictOrNull() ?: false
        set(value) = store.write(mapOf(FINGERPRINT to value.toString()))

    override fun saveSelection(selection: UserSelection) {
        store.write(
            mapOf(
                CURRENCY to selection.spinnerSelection,
                DOLLAR_RATE to selection.dollarInput.scaledValue.toString(),
                EURO_RATE to selection.euroInput.scaledValue.toString(),
            )
        )
    }

    override fun loadSelection(): UserSelection? {
        return UserSelection(
            spinnerSelection = store.read(CURRENCY) ?: return null,
            dollarInput = dollarRate ?: return null,
            euroInput = euroRate ?: return null,
        )
    }

    override fun deleteSelection() = store.write(
        mapOf(CURRENCY to null, DOLLAR_RATE to null, EURO_RATE to null)
    )

    override fun hasSelection(): Boolean = store.read(CURRENCY) != null

    private fun readRate(key: String): ExchangeRate? {
        val value = store.read(key) ?: return null
        return value.toLongOrNull()?.let(::ExchangeRate) ?: ExchangeRate.parse(value)
    }

    private companion object {
        const val FOP_GROUP = "fop_group"
        const val GROUP_3_RATE = "fop_group_3_rate"
        const val CURRENCY = "spinner_selection"
        const val DOLLAR_RATE = "dollar_input"
        const val EURO_RATE = "euro_input"
        const val APP_LOCK = "switch_app_lock"
        const val FINGERPRINT = "switch_fingerprint_unlock"
    }
}
