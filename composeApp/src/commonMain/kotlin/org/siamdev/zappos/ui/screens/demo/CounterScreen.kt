package org.siamdev.zappos.ui.screens.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.BottomSheet
import org.siamdev.zappos.ui.components.MaterialButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterScreen() {
    var count by remember { mutableIntStateOf(0) }

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )
    val scope = rememberCoroutineScope()

    BottomSheet(
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
                    onClick = { count = 0 }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Count: $count", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                MaterialButton(
                    text = "-",
                    onClick = { count-- },
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                MaterialButton(
                    text = "+",
                    onClick = { count++ },
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(Modifier.height(20.dp))

            Button(onClick = {
                scope.launch {
                    sheetState.bottomSheetState.expand()
                }
            }) {
                Text("Open BottomSheet")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    CounterScreen()
}
