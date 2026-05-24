/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting.currency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.data.source.local.CurrencyItem
import org.siamdev.zappos.ui.components.common.CurrencyCodeIcon
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.bitcoin_unit
import zappos.composeapp.generated.resources.sat_unit

@Composable
fun CurrencySettingScreen(onNavigateBack: () -> Unit = {}) {
    val viewModel     = LocalSettingVM.current
    val currencies    by viewModel.currencies.collectAsState()
    val primaryCcy    by viewModel.primaryCurrency.collectAsState()
    val secondaryCcy  by viewModel.secondaryCurrency.collectAsState()
    val showSecondary by viewModel.showSecondaryCurrency.collectAsState()
    val isLoading     by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        WorkspaceHeader(title = "Currency", onNavigateBack = onNavigateBack)

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                SettingsCard(
                    currencies    = currencies,
                    primaryCcy    = primaryCcy,
                    secondaryCcy  = secondaryCcy,
                    showSecondary = showSecondary,
                    onSelectPrimary   = { viewModel.selectPrimaryCurrency(it) },
                    onSelectSecondary = { viewModel.selectSecondaryCurrency(it) },
                    onToggleSecondary = { viewModel.toggleSecondaryCurrency(it) }
                )
            }
        }
    }
}

@Composable
private fun SettingsCard(
    currencies: List<CurrencyItem>,
    primaryCcy: CurrencyItem?,
    secondaryCcy: CurrencyItem?,
    showSecondary: Boolean,
    onSelectPrimary: (String) -> Unit,
    onSelectSecondary: (String) -> Unit,
    onToggleSecondary: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        SettingSection(label = "PRIMARY CURRENCY") {
            currencies.forEach { ccy ->
                CurrencyCard(
                    currency = ccy,
                    isActive = ccy.id == primaryCcy?.id,
                    onClick  = { onSelectPrimary(ccy.id) }
                )
            }
        }

        SectionDivider()

        SettingSection(label = "SECONDARY CURRENCY") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Show alongside prices",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Displays a second currency next to every amount",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                }
                Switch(
                    checked = showSecondary,
                    onCheckedChange = onToggleSecondary,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            AnimatedVisibility(visible = showSecondary) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    currencies.forEach { ccy ->
                        CurrencyCard(
                            currency = ccy,
                            isActive = ccy.id == secondaryCcy?.id,
                            onClick  = { onSelectSecondary(ccy.id) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SettingSection(label: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            letterSpacing = 2.sp
        )
        content()
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
}

@Composable
private fun CurrencyCard(currency: CurrencyItem, isActive: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                else MaterialTheme.colorScheme.background
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyIcon(code = currency.code, isActive = isActive)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = currency.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = currency.code,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }
        RadioButton(
            selected = isActive,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun CurrencyIcon(code: String, isActive: Boolean) {
    val bg = when (code) {
        "BTC" -> Color.Transparent
        else  -> if (isActive) MaterialTheme.colorScheme.primary
                 else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    }
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        when (code) {
            "BTC"  -> Icon(painterResource(Res.drawable.bitcoin_unit), null,
                tint = Color.Unspecified, modifier = Modifier.size(38.dp))
            "SATS" -> Icon(painterResource(Res.drawable.sat_unit), null,
                tint = if (isActive) Color.White else Color(0xFFFFB700),
                modifier = Modifier.size(22.dp))
            else   -> CurrencyCodeIcon(
                code = code,
                modifier = Modifier.size(22.dp),
                tint = if (isActive) Color.White else MaterialTheme.colorScheme.primary
            )
        }
    }
}