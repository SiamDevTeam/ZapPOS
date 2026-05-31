/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

data class TabItem(
    val label: String,
    val icon: ImageVector? = null
)

@Composable
fun SegmentedTabBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = index == selectedIndex
            val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
                               else MaterialTheme.colorScheme.onSurfaceVariant
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onTabSelect(index) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (tab.icon != null) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = contentColor
                    )
                    Spacer(Modifier.width(6.dp))
                }
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = contentColor,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun SegmentedTabBarWithIconsPreview() {
    MaterialTheme {
        var selected by remember { mutableIntStateOf(0) }
        SegmentedTabBar(
            tabs = listOf(
                TabItem("Product Detail", Icons.Default.Info),
                TabItem("Monitor & Stock-In", Icons.Default.Inventory)
            ),
            selectedIndex = selected,
            onTabSelect = { selected = it },
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun SegmentedTabBarTextOnlyPreview() {
    MaterialTheme {
        var selected by remember { mutableIntStateOf(0) }
        SegmentedTabBar(
            tabs = listOf(TabItem("Private Key"), TabItem("Seed Phrase")),
            selectedIndex = selected,
            onTabSelect = { selected = it },
            modifier = Modifier.padding(12.dp),
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
        )
    }
}