package com.example.businesstaxcalculator.presentation.profile.subview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.domain.fop.FopGroup

@Composable
fun FopGroupSelector(selected: FopGroup, onSelect: (FopGroup) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp))
    {
        FopGroup.entries.forEach { group ->
            SelectionRow(
                text = stringResource(R.string.fop_group_number, group.number),
                selected = group == selected,
                onClick = { onSelect(group) }
            )
        }
    }
}