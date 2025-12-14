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
package org.siamdev.zappos.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.di.viewModelOf
import org.siamdev.zappos.ui.components.MaterialButton
import org.siamdev.zappos.ui.components.MenuItemCard
import org.siamdev.zappos.ui.components.OrderItemCard
import org.siamdev.zappos.ui.components.SlideBottomSheet
import org.siamdev.zappos.ui.components.TopBar
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen() {
    val viewModel = viewModelOf { MainMenuViewModel() }
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            //initialValue = SheetValue.Expanded,
            skipHiddenState = true
        )
    )

    val items = viewModel.items

    SlideBottomSheet(
        sheetState = sheetState,
        sheetMaxHeight = 420.dp,
        topContent = {
            viewModel.selectedKeys.forEach { key ->
                val item = items.first { it.id == key }
                OrderItemCard(
                    item = item,
                    onAddClick = { viewModel.addItem(item.id) },
                    onReduceClick = { viewModel.reduceItem(item.id) }
                )
            }
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

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CurrencyLira,
                                contentDescription = "Currency",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(viewModel.totalFiat, style = MaterialTheme.typography.titleLarge)
                        }

                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(painterResource(
                                resource = Res.drawable.sat_unit),
                                contentDescription = "sat icon",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(viewModel.totalSat, style = MaterialTheme.typography.titleMedium)
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
                    modifier = Modifier
                        .weight(1f),
                    text = "Clear Cart",
                    buttonColor = Color.White,
                    showBorder = true,
                    onClick = { viewModel.clearAllItems() }
                )

                Spacer(Modifier.width(12.dp))

                MaterialButton(
                    modifier = Modifier.weight(1f),
                    text = "Checkout",
                    iconStart = Icons.Default.ShoppingCart,
                    onClick = {  }
                )
            }
        }
    ) {
        // Screen conten
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Top Header
            TopBar(
                title = "ZapPOS",
                modifier = Modifier
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = "Currency",
                    modifier = Modifier.size(27.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Menus",
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- Detail Screen ---
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                    .padding(top = 5.dp, bottom = 100.dp)
            ) {
                val horizontalPadding = if (maxWidth > 800.dp) {
                    (maxWidth - 650.dp) / 2
                } else {
                    16.dp
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = horizontalPadding, end = horizontalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(items.size) { index ->
                            val item = items[index]
                            MenuItemCard(
                                imageUrl = item.imageUrl,
                                name = item.name,
                                priceBaht = item.priceBaht,
                                priceSat = item.priceSat,
                                count = item.count,
                                onAddClick = { viewModel.addItem(item.id) },
                                onReduceClick = { viewModel.reduceItem(item.id) }
                            )
                        }
                    }
                }
            }


        }
    }
}

@Preview
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen()
}
