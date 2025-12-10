package org.siamdev.zappos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit
import org.jetbrains.compose.resources.painterResource

@Composable
fun MenuItemCard(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String,
    count: UInt = 0u,
    onAddClick: () -> Unit = {},
    onReduceClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))


            // Middle: Item Info
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CurrencyLira,
                        contentDescription = "Currency",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = priceBaht, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Res.drawable.sat_unit),
                        contentDescription = "sat icon",
                        tint = Color(0xFFFFB700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = priceSat,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFFB700)
                    )
                }
            }


            // Right: Qty + Add button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Bottom,
                verticalArrangement = Arrangement.Center

            ) {

                Box(
                    modifier = Modifier.height(35.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (count > 0u) {
                        Text(
                            text = "x$count",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))


                MaterialButton(
                    modifier = Modifier.size(40.dp),
                    iconCenter = Icons.Default.Add,
                    iconColor = Color.White,
                    buttonColor = Color(0xFF070E1E),
                    onClick = onAddClick
                )
            }

                Spacer(Modifier.width(12.dp))
            }
        }

}



@Preview
@Composable
fun Item1CardPreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg",
        name = "Americano",
        priceBaht = "70.00",
        priceSat = "17,500",
        count = 21u
    )
}

