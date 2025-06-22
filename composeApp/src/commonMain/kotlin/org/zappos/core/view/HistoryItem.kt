package org.zappos.core.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        // click to see bill detail
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
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = date,
                    color = Color.LightGray,
                    fontSize = 16.sp
                )
            }
            Column (
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = sats,
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = baht,
                    color = Color.LightGray,
                    fontSize = 16.sp
                )
            }
        }
    }
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