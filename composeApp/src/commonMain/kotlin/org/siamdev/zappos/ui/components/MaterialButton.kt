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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    iconSize: Int = 24,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
) {

    val contentPadding =
        if (iconCenter != null) PaddingValues(0.dp)
        else PaddingValues(horizontal = 12.dp, vertical = 6.dp)

    Button(
        modifier = modifier
            .background(
                color = buttonColor,
                shape = RoundedCornerShape(8.dp)
            ),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = contentPadding
    ) {

        if (iconCenter != null) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(iconSize.dp)
            ) {
                Icon(
                    imageVector = iconCenter,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize.dp)
                )
            }
            return@Button
        }

        Row(horizontalArrangement = Arrangement.Center) {

            // Start Icon (optional)
            if (iconStart != null) {
                Icon(
                    imageVector = iconStart,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            //Text(text)
            if (text.isNotEmpty()) Text(text)

            // End Icon (optional)
            if (iconEnd != null) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = iconEnd,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun MaterialButtonPreview() {
    MaterialButton(
        modifier = Modifier.width(250.dp),
        text = "Preview Button",
        iconStart = Icons.Default.ShoppingCart,
        iconEnd = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        onClick = {}
    )
}

@Preview
@Composable
fun AddButtonPreview() {
    MaterialButton(
        modifier = Modifier.size(50.dp),
        iconCenter = Icons.Default.Add,
        iconSize = 28,
        buttonColor = Color(0xFF070E1E),
        onClick = {}
    )
}

