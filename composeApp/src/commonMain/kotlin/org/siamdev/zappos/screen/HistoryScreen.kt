package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.TransactionHistory
import org.siamdev.zappos.TransactionItem
import org.siamdev.zappos.view.HistoryItem
import org.siamdev.zappos.view.TransactionMenuItem

@Composable
fun HistoryScreen(
    totalSats: String = "0",
    totalBaht: String = "0",
    historyList: List<TransactionHistory>
) {
    var selectedTransaction by remember { mutableStateOf<TransactionHistory?>(null) }
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = totalSats,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = totalBaht,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column {
            historyList.forEach { item ->
                HistoryItem(
                    name = item.transactionId,
                    date = item.date,
                    sats = item.totalSats,
                    baht = item.totalBaht,
                    onClick = { selectedTransaction = item }
                )
            }
        }
    }
    selectedTransaction?.let {
        AlertDialog(
            onDismissRequest = { selectedTransaction = null },
            title = { Text(text = "Transaction Detail") },
            text = {
                Column {
                    Text(
                        text = "Transaction ID",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = it.transactionId,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Date & Time",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = it.date,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Items",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Column {
                        it.items.forEach { item ->
                            TransactionMenuItem(
                                itemName = item.itemName,
                                quantity = item.quantity,
                                sats = item.sats,
                                baht = item.baht
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(thickness = 2.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Amount"
                        )
                        Column (
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = it.totalSats,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                            Text(
                                text = it.totalBaht,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedTransaction = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(
        totalSats = "10000 sats",
        totalBaht = "about ฿21.00",
        historyList = listOf(
            TransactionHistory(
                transactionId = "1",
                date = "June 1, 2025",
                totalSats = "1000 sats",
                totalBaht = "฿20.00",
                items = listOf(
                    TransactionItem("Item 1", "2", "500 sats", "฿10.00"),
                    TransactionItem("Item 2", "1", "500 sats", "฿10.00")
                )
            ),
            TransactionHistory(
                transactionId = "2",
                date = "June 2, 2025",
                totalSats = "2000 sats",
                totalBaht = "฿40.00",
                items = listOf(
                    TransactionItem("Item 3", "4", "1000 sats", "฿20.00"),
                    TransactionItem("Item 4", "2", "1000 sats", "฿20.00")
                )
            ),
            TransactionHistory(
                transactionId = "3",
                date = "June 3, 2025",
                totalSats = "3000 sats",
                totalBaht = "฿60.00",
                items = listOf(
                    TransactionItem("Item 5", "3", "1500 sats", "฿30.00"),
                    TransactionItem("Item 6", "3", "1500 sats", "฿30.00")
                )
            )
        )
    )
}
