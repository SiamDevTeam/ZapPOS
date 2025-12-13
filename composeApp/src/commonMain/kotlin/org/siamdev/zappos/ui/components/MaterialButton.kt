/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.zappos.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MaterialButton(
    modifier: Modifier = Modifier,
    text: String = "",
    iconStart: ImageVector? = null,
    iconCenter: ImageVector? = null,
    iconEnd: ImageVector? = null,
    iconSize: Int = 25,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    showBorder: Boolean = false,
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            isPressed = interaction is PressInteraction.Press
        }
    }

    val targetColor = if (isPressed) buttonColor.copy(alpha = 0.6f) else buttonColor

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 150)
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 90)
    )

    val contentPadding =
        if (iconCenter != null) PaddingValues(0.dp)
        else PaddingValues(horizontal = 12.dp, vertical = 8.dp)

    Box(
        modifier = modifier
            .scale(scale)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .then(
                if (showBorder) Modifier.border(
                    width = 1.dp,
                    color = Color(0xFFD9D9D9),
                    shape = RoundedCornerShape(8.dp)
                ) else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(contentPadding)
            .heightIn(min = 40.dp)
    ) {

        if (iconCenter != null) {
            Icon(
                imageVector = iconCenter,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize.dp)
                    .align(Alignment.Center),
                tint = iconColor
            )
            return@Box
        }

        // Normal button layout: start icon + text + end icon
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            // Optional start icon
            if (iconStart != null) {
                Icon(
                    imageVector = iconStart,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconColor
                )
                Spacer(Modifier.width(8.dp))
            }

            // Optional text
            if (text.isNotEmpty()) Text(text, color = iconColor)

            // Optional end icon
            if (iconEnd != null) {
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = iconEnd,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconColor
                )
            }
        }
    }
}


@Preview
@Composable
fun MaterialButtonPreview() {
    MaterialTheme {
        MaterialButton(
            modifier = Modifier.width(250.dp),
            text = "Preview Button",
            iconStart = Icons.Default.ShoppingCart,
            iconEnd = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun AddButtonPreview() {
    MaterialTheme {
        MaterialButton(
            modifier = Modifier.size(50.dp),
            iconCenter = Icons.Default.Add,
            iconSize = 28,
            buttonColor = Color(0xFF070E1E),
            onClick = {}
        )
    }
}
