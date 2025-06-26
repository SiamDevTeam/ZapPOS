package org.siamdev.zappos.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HistoryItem(
    name: String,
    date: String,
    sats: String,
    baht: String,
    onClick: () -> Unit
) {
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = onClick
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = date,
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
            Column (
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = sats,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = baht,
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TransactionMenuItem(
    itemName: String,
    quantity: String,
    sats: String,
    baht: String,
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "$itemName x $quantity",
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = sats,
                fontSize = 18.sp
            )
            Text(
                text = baht,
                fontSize = 14.sp
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview
@Composable
fun previewHistoryItem() {
    HistoryItem(
        name = "#1750582851547",
        date = "6/22/2025, 4:00:51 PM",
        sats = "+ 1000 Sats",
        baht = "about ฿21.00",
        onClick = {}
    )
}

@Preview
@Composable
fun previewTransactionMenuItem() {
    TransactionMenuItem(
        itemName = "Item Name",
        quantity = "2",
        sats = "500 sat",
        baht = "10.00 baht"
    )
}