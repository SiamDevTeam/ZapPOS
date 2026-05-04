/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.glow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.GlowEffects
import org.siamdev.zappos.ui.components.GlowShape
import org.siamdev.zappos.ui.components.WorkspaceHeader

@Composable
fun BalancedRow(
    modifier: Modifier = Modifier,
    spacing: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = spacing,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun GlowStyleScreen(
    onOpenDrawer: () -> Unit = {}
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            WorkspaceHeader(title = "Glow Effects", onSegmentClick = onOpenDrawer)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BalancedRow {
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Square
                    )
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Circle
                    )
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Rounded
                    )
                }


                BalancedRow {
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Square,
                        coreIntensity = 1.6f,
                        glowIntensity = 0.6f
                    )
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Circle,
                        coreIntensity = 1.8f,
                        glowIntensity = 1.0f
                    )
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Rounded,
                        coreIntensity = 0.7f,
                        glowIntensity = 1.2f
                    )
                }

                BalancedRow {
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Square,
                        drawCore = false,
                        glowIntensity = 1.0f
                    )
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Circle,
                        drawCore = false,
                        glowIntensity = 0.8f
                    )
                    GlowEffects(
                        modifier = Modifier.weight(1f),
                        shape = GlowShape.Rounded,
                        drawCore = false,
                        glowIntensity = 1.3f
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun GlowStyleScreenPreview() {
    GlowStyleScreen()
}