/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components


import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideBottomSheet(
    sheetState: BottomSheetScaffoldState,
    sheetMaxHeight: Dp = 320.dp,
    topContent: @Composable ColumnScope.() -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit,
    screenContent: @Composable () -> Unit
) {

    val customDragHandle: @Composable (() -> Unit) = {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(30.dp),
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
        sheetPeekHeight = 76.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetDragHandle = customDragHandle,
        sheetShadowElevation = 10.dp,
        sheetContainerColor = MaterialTheme.colorScheme.surface,

        // BottomSheet content
        sheetContent = {
            BottomSheetInternalLayout(
                sheetMaxHeight = sheetMaxHeight,
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
    sheetMaxHeight: Dp,
    topContent: @Composable ColumnScope.() -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .height(sheetMaxHeight)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(Modifier.height(16.dp))

        // Bottom content fixed
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically),
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
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
            sheetMaxHeight = 420.dp,
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(Res.drawable.sat_unit),
                                    contentDescription = "sat icon",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("1,900.35", style = MaterialTheme.typography.titleMedium)
                            }
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