package org.siamdev.zappos.ui.screens.setting.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.siamdev.zappos.data.source.local.CurrencyItem
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.sale.checkout.SectionLabel
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.bitcoin_unit
import zappos.composeapp.generated.resources.sat_unit

@Composable
fun CurrencySettingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingViewModel
) {
    val currencies by viewModel.currencies.collectAsState()
    val primaryCurrency by viewModel.primaryCurrency.collectAsState()
    val secondaryCurrency by viewModel.secondaryCurrency.collectAsState()
    val showSecondary by viewModel.showSecondaryCurrency.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        WorkspaceHeader(title = "Currency", onNavigateBack = onNavigateBack)

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = YellowPrimary)
            }
            return@Column
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SectionLabel(
                    text = "PRIMARY CURRENCY",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(currencies) { currency ->
                CurrencyCard(
                    currency = currency,
                    isActive = currency.id == primaryCurrency?.id,
                    onClick = { viewModel.selectPrimaryCurrency(currency.id) }
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            item {
                SecondaryCurrencyHeader(
                    enabled = showSecondary,
                    onToggle = { viewModel.toggleSecondaryCurrency(it) }
                )
            }

            if (showSecondary) {
                items(currencies) { currency ->
                    CurrencyCard(
                        currency = currency,
                        isActive = currency.id == secondaryCurrency?.id,
                        onClick = { viewModel.selectSecondaryCurrency(currency.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SecondaryCurrencyHeader(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            SectionLabel(text = "SECONDARY CURRENCY")
            Text(
                text = "Show a second currency alongside prices",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = YellowPrimary
            )
        )
    }
}

@Composable
private fun CurrencyCard(currency: CurrencyItem, isActive: Boolean, onClick: () -> Unit) {
    val bg = if (isActive) YellowPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isActive)
                    YellowPrimary.copy(alpha = 0.4f)
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(bg)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyIcon(code = currency.code, symbol = currency.symbol, isActive = isActive)

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = currency.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = currency.code,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        RadioButton(
            selected = isActive,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = YellowPrimary)
        )
    }
}

@Composable
private fun CurrencyIcon(code: String, symbol: String, isActive: Boolean) {
    val boxBg = when (code) {
        "BTC" -> Color.Transparent
        else -> if (isActive) YellowPrimary else YellowPrimary.copy(alpha = 0.15f)
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(boxBg),
        contentAlignment = Alignment.Center
    ) {
        when (code) {
            "BTC" -> Icon(
                painter = painterResource(Res.drawable.bitcoin_unit),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(38.dp)
            )
            "SATS" -> Icon(
                painter = painterResource(Res.drawable.sat_unit),
                contentDescription = null,
                tint = if (isActive) Color.White else Color(0xFFFFB700),
                modifier = Modifier.size(22.dp)
            )
            else -> Text(
                text = symbol,
                color = if (isActive) Color.White else YellowPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}