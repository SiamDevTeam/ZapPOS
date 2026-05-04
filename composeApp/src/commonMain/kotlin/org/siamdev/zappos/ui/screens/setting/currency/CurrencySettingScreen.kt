package org.siamdev.zappos.ui.screens.setting.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.data.source.local.CurrencyItem
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

private enum class CurrencyTab { PRIMARY, SECONDARY }

@Composable
fun CurrencySettingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingViewModel
) {
    val currencies by viewModel.currencies.collectAsState()
    val primaryCurrency by viewModel.primaryCurrency.collectAsState()
    val secondaryCurrency by viewModel.secondaryCurrency.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTab by remember { mutableStateOf(CurrencyTab.PRIMARY) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        CurrencyHeader(onNavigateBack)

        SecondaryTabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.surface,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedTabIndex = selectedTab.ordinal),
                    color = YellowPrimary
                )
            }
        ) {
            Tab(
                selected = selectedTab == CurrencyTab.PRIMARY,
                onClick = { selectedTab = CurrencyTab.PRIMARY },
                text = { Text("Primary") },
                selectedContentColor = YellowPrimary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Tab(
                selected = selectedTab == CurrencyTab.SECONDARY,
                onClick = { selectedTab = CurrencyTab.SECONDARY },
                text = { Text("Secondary") },
                selectedContentColor = YellowPrimary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = YellowPrimary)
            }
            return@Column
        }

        val activeId = when (selectedTab) {
            CurrencyTab.PRIMARY -> primaryCurrency?.id
            CurrencyTab.SECONDARY -> secondaryCurrency?.id
        }

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                val hint = when (selectedTab) {
                    CurrencyTab.PRIMARY -> "Select the main currency displayed in prices"
                    CurrencyTab.SECONDARY -> "Optional secondary currency shown alongside prices"
                }
                Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
            }

            items(currencies) { currency ->
                CurrencyCard(
                    currency = currency,
                    isActive = currency.id == activeId,
                    onClick = {
                        when (selectedTab) {
                            CurrencyTab.PRIMARY -> viewModel.selectPrimaryCurrency(currency.id)
                            CurrencyTab.SECONDARY -> viewModel.selectSecondaryCurrency(currency.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CurrencyHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(YellowPrimary.copy(alpha = 0.15f))
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text("Currency", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun CurrencyCard(currency: CurrencyItem, isActive: Boolean, onClick: () -> Unit) {
    val bg = if (isActive) YellowPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
    val iconBg = if (isActive) YellowPrimary else YellowPrimary.copy(alpha = 0.15f)
    val symbolColor = if (isActive) Color.White else YellowPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Text(currency.symbol, color = symbolColor, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(
                currency.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(currency.code, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        RadioButton(
            selected = isActive,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = YellowPrimary)
        )
    }
}