package com.example.businesstaxcalculator.presentation.profile

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.example.businesstaxcalculator.utils.FOP_GROUP_3_RATE_PREFERENCE
import com.example.businesstaxcalculator.utils.FOP_GROUP_PREFERENCE
import com.example.businesstaxcalculator.utils.FopGroup
import com.example.businesstaxcalculator.utils.Group3TaxRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ProfileUiState(
    val group: FopGroup,
    val group3Rate: Group3TaxRate
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferences: SharedPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ProfileUiState(
            group = FopGroup.from(
                preferences.getInt(FOP_GROUP_PREFERENCE, FopGroup.FIRST.number)
            ),
            group3Rate = Group3TaxRate.from(
                preferences.getInt(
                    FOP_GROUP_3_RATE_PREFERENCE,
                    Group3TaxRate.WITHOUT_VAT.percent
                )
            )
        )
    )
    val uiState = _uiState.asStateFlow()

    fun selectGroup(group: FopGroup) {
        _uiState.update { it.copy(group = group) }
        preferences.edit { putInt(FOP_GROUP_PREFERENCE, group.number) }
    }

    fun selectGroup3Rate(rate: Group3TaxRate) {
        _uiState.update { it.copy(group3Rate = rate) }
        preferences.edit { putInt(FOP_GROUP_3_RATE_PREFERENCE, rate.percent) }
    }
}
