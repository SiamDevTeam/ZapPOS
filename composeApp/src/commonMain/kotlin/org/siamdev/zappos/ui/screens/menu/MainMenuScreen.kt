package org.siamdev.zappos.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.MaterialButton
import org.siamdev.zappos.ui.components.MenuItemCard
import org.siamdev.zappos.ui.components.SlideBottomSheet
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen() {

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    val scope = rememberCoroutineScope()

    SlideBottomSheet(
        sheetState = sheetState,
        topContent = {
            Text("Matcha Coffee 999")
            Text("Latte 999")
            Text("Mocha 999")
            Text("Americano 999")
            Text("Americano 999")
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

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CurrencyLira,
                                contentDescription = "Currency",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("61,020", style = MaterialTheme.typography.titleLarge)
                        }

                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(painterResource(
                                resource = Res.drawable.sat_unit),
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
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    text = "Clear Cart",
                    buttonColor = Color.White,
                    onClick = {  }
                )

                Spacer(Modifier.width(12.dp))

                MaterialButton(
                    modifier = Modifier.weight(1f),
                    text = "Confirm",
                    iconStart = Icons.AutoMirrored.Filled.Article,
                    onClick = {  }
                )
            }
        }
    ) {
        // Screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg",
                        name = "Americano",
                        priceBaht = "70.00",
                        priceSat = "17,500"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg",
                        name = "Americano",
                        priceBaht = "70.00",
                        priceSat = "17,500"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg",
                        name = "Americano",
                        priceBaht = "100.00",
                        priceSat = "26,000"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                        name = "Americano",
                        priceBaht = "100.00",
                        priceSat = "26,000"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                        name = "Americano",
                        priceBaht = "100.00",
                        priceSat = "26,000"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                        name = "Americano",
                        priceBaht = "100.00",
                        priceSat = "26,000"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                        name = "Americano",
                        priceBaht = "100.00",
                        priceSat = "26,000"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                        name = "Americano",
                        priceBaht = "100.00",
                        priceSat = "26,000"
                    )
                }

                item {
                    MenuItemCard(
                        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                        name = "Americano",
                        priceBaht = "100.00",
                        priceSat = "26,000"
                    )
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
