package com.example.businesstaxcalculator.presentation.profile

import com.example.businesstaxcalculator.domain.settings.AppSettings
import androidx.lifecycle.ViewModel
import com.example.businesstaxcalculator.domain.fop.FopGroup
import com.example.businesstaxcalculator.domain.fop.Group3TaxRate
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
    private val settings: AppSettings
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ProfileUiState(
            group = settings.fopGroup,
            group3Rate = settings.group3TaxRate
        )
    )
    val uiState = _uiState.asStateFlow()

    fun selectGroup(group: FopGroup) {
        _uiState.update { it.copy(group = group) }
        settings.fopGroup = group
    }

    fun selectGroup3Rate(rate: Group3TaxRate) {
        _uiState.update { it.copy(group3Rate = rate) }
        settings.group3TaxRate = rate
    }
}
