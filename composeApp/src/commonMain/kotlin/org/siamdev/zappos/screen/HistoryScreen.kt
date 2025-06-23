package org.siamdev.zappos.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.view.HistoryItem

@Composable
fun HistoryScreen(
    totalSats: String = "0",
    totalBaht: String = "0",
    historyList: List<TransactionHistory>,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
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
        LazyColumn {
            historyList.forEach { item ->
                item {
                    HistoryItem(
                        name = item.name,
                        date = item.date,
                        sats = item.sats,
                        baht = item.baht,
                        onClick = { onItemClick(item.transactionId) }
                    )
                }
            }
        }
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
                name = "Transaction 1",
                date = "June 1, 2025",
                sats = "1000 sats",
                baht = "฿20.00"
            ),
            TransactionHistory(
                transactionId = "2",
                name = "Transaction 2",
                date = "June 2, 2025",
                sats = "2000 sats",
                baht = "฿40.00"
            ),
            TransactionHistory(
                transactionId = "3",
                name = "Transaction 3",
                date = "June 3, 2025",
                sats = "3000 sats",
                baht = "฿60.00"
            )
        ),
        onItemClick = {}
    )
}

//  TODO: will move
class TransactionHistory(
    val transactionId: String,
    val name: String,
    val date: String,
    val sats: String,
    val baht: String
) {
    // Add any additional properties or methods if needed
}