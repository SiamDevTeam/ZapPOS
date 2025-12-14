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
package org.siamdev.zappos.ui.screens.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.MaterialButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import org.siamdev.zappos.ui.components.GlowEffects
import org.siamdev.zappos.ui.components.GlowShape


@Composable
fun CounterScreen() {
    var count by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
            //.background(Color.Black)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(50.dp)
        ) {
            // Layer 2
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {

                    // Layer 1: Glow (อยู่หลัง ZP)
                    GlowEffects(
                        modifier = Modifier
                            .matchParentSize(),
                        shape = GlowShape.Square,
                        drawCore = false,
                        glowIntensity = 0.5f
                    )

                    // Layer 2: ZP Core
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color(0xFFFCBE00),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "ZP",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "ZapPOS",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Count: $count", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                MaterialButton(
                    modifier = Modifier.size(50.dp),
                    iconCenter = Icons.Default.Remove,
                    iconSize = 28,
                    onClick = { count-- }
                )
                Spacer(modifier = Modifier.width(16.dp))

                MaterialButton(
                    modifier = Modifier.size(50.dp),
                    iconCenter = Icons.Default.Add,
                    iconSize = 28,
                    onClick = { count++ }
                )
            }
            Spacer(Modifier.height(20.dp))

            MaterialButton(
                modifier = Modifier.width(117.dp),
                text = "reset count",
                onClick = { count = 0 }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    CounterScreen()
}
