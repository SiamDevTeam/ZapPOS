package org.siamdev.zappos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onAddClick: () -> Unit = {}
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
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(0.1f)
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
                        modifier = Modifier.size(20.dp)
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
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = priceSat,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFFB700)
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(80.dp)
            ) {
                MaterialButton(
                    modifier = Modifier.size(36.dp),
                    iconCenter = Icons.Default.Add,
                    iconSize = 37,
                    buttonColor = Color(0xFF070E1E),
                    onClick = onAddClick
                )
            }
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
        priceSat = "17,500"
    )
}


/*
@Preview
@Composable
fun MenuItemCardPreview() {
    // https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg | 17,500 sat 70.00 baht
    // https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg | 17,500 sat 70.00 baht
    // https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg | 26,000 sat 100.00 baht
    // https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg | 26,000 sat 100.00 baht
}
*/
