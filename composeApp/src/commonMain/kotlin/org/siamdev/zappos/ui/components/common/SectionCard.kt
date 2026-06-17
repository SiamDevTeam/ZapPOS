/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SectionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badge: String? = null,
    expanded: Boolean? = null,
    onExpandChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val chevronAngle by animateFloatAsState(
        targetValue = if (expanded == true) 180f else 0f,
        label = "chevron"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (expanded != null && onExpandChange != null)
                        Modifier.clickable { onExpandChange(!expanded) }
                    else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (badge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (expanded != null) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp).rotate(chevronAngle)
                )
            }
        }

        if (expanded != null) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = content
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

@Preview(showBackground = true, name = "SectionCard - Standard")
@Composable
private fun SectionCardStandardPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            SectionCard(
                icon = Icons.Default.KeyboardArrowDown,
                title = "Appearance",
                subtitle = "Customizing theme and fonts"
            ) {
                Text("Content inside the card goes here.")
            }
        }
    }
}

@Preview(showBackground = true, name = "SectionCard - With Badge")
@Composable
private fun SectionCardWithBadgePreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            SectionCard(
                icon = Icons.Default.KeyboardArrowDown,
                title = "Currencies",
                subtitle = "Active currency pairs",
                badge = "2 Active"
            ) {
                Text("Thai Baht (THB)")
                Text("Sats (BTC)")
            }
        }
    }
}

@Preview(showBackground = true, name = "SectionCard - Expandable (Collapsed)")
@Composable
private fun SectionCardExpandableCollapsedPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            SectionCard(
                icon = Icons.Default.KeyboardArrowDown,
                title = "Expandable Section",
                subtitle = "Click to see more",
                expanded = false,
                onExpandChange = {}
            ) {
                Text("This should be hidden")
            }
        }
    }
}

@Preview(showBackground = true, name = "SectionCard - Expandable (Expanded)")
@Composable
private fun SectionCardExpandableExpandedPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            SectionCard(
                icon = Icons.Default.KeyboardArrowDown,
                title = "Collapsible Section",
                subtitle = "Currently showing details",
                expanded = true,
                onExpandChange = {}
            ) {
                Text("Detail Item 1")
                Text("Detail Item 2")
                Text("Detail Item 3")
            }
        }
    }
}

@Preview(showBackground = true, name = "SectionCard - Interactive")
@Composable
private fun SectionCardInteractivePreview() {
    MaterialTheme {
        var isExpanded by remember { mutableStateOf(true) }
        Box(Modifier.padding(16.dp)) {
            SectionCard(
                icon = Icons.Default.KeyboardArrowDown,
                title = "Interactive Section",
                subtitle = "Click header to toggle",
                expanded = isExpanded,
                onExpandChange = { isExpanded = it }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { i ->
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Text(
                                "Dynamic Item ${i + 1}",
                                modifier = Modifier.padding(12.dp).fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
