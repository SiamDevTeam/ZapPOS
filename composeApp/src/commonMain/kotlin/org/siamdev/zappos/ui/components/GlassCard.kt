/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    horizontalAlignment : Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(24.dp)

    Column(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = shape
            )
            .padding(horizontal = 28.dp, vertical = 36.dp)
            .graphicsLayer { alpha = 0.99f },
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}
@Preview(showBackground = true, widthDp = 411)
@Composable
fun GlassCardPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
    ) {


        GlassCard(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
        ) {
            Text(
                text = "Glass Card",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This is a reusable glassmorphism component.",
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}