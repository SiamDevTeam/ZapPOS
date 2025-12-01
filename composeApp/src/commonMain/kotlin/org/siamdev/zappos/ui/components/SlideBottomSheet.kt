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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideBottomSheet(
    topContent: @Composable ColumnScope.() -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit,
    sheetState: BottomSheetScaffoldState,
    screenContent: @Composable () -> Unit
) {

    val customDragHandle: @Composable (() -> Unit) = {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 100.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetDragHandle = customDragHandle,
        sheetShadowElevation = 10.dp,
        sheetContainerColor = Color.White,

        // BottomSheet content
        sheetContent = {
            BottomSheetInternalLayout(
                topContent = topContent,
                bottomContent = bottomContent
            )
        },
        // Main screen content
        content = {
            screenContent()
        }
    )

}


@Composable
private fun BottomSheetInternalLayout(
    topContent: @Composable ColumnScope.() -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .height(320.dp)
            .padding(16.dp)
    ) {
        // Top content scrollable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            topContent()
        }

        Spacer(Modifier.height(16.dp))

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color(0xFFC4C4C4)
        )

        Spacer(Modifier.height(16.dp))

        // Bottom content fixed
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            bottomContent()
        }
    }
}




@Preview
@Composable
fun BottomSheetV1Preview() {
    Surface {
        BottomSheetInternalLayout(
            topContent = {
                Text("Matcha Coffee 999")
                Text("Latte 999")
                Text("Mocha 999")
                Text("Americano 999")
            },
            bottomContent = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Total Payment",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )

                    Box(
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text("฿ 61,020", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(4.dp))
                            Text("$ 1,900.35", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }


                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MaterialButton(
                        modifier = Modifier.weight(1f),
                        text = "Clear Cart",
                        buttonColor = Color.White,
                        onClick = {  }
                    )

                    Spacer(Modifier.width(12.dp))

                    MaterialButton(
                        modifier = Modifier.weight(1f),
                        text = "Confirm",
                        iconStart = Icons.Default.ShoppingCart,
                        onClick = {  }
                    )
                }
            }

        )

    }
}