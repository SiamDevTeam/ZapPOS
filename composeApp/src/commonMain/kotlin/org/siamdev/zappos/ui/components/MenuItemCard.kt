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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit
import org.jetbrains.compose.resources.painterResource
import zappos.composeapp.generated.resources.compose_multiplatform

@Composable
fun MenuItemCard(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String? = null,
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
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageUrl)
                    .listener(
                        onStart = { println("Coil start: ${it.data}") },
                        onSuccess = { _, _ -> println("Coil success") },
                        onError = { _, result -> result.throwable.printStackTrace() }
                    )
                    .build(),
                contentDescription = name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.LightGray),
                error = painterResource(Res.drawable.compose_multiplatform)
            )

            Spacer(Modifier.width(12.dp))


            // Middle: Item Info
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    fontSize = 18.sp
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

                // Sat price row
                if (priceSat != null) {
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
        name = "Mocha",
        priceBaht = "70.00",
        priceSat = "17,500",
        count = 21u
    )
}

@Preview
@Composable
fun Item2CardPreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg",
        name = "Latte",
        priceBaht = "70.00",
        priceSat = "17,500",
        count = 2u
    )
}


@Preview
@Composable
fun Item3CardPreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg",
        name = "Matcha Latte",
        priceBaht = "100.00",
        //priceSat = "26,000",
        count = 4u
    )
}


@Preview
@Composable
fun Item4CardPreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
        name = "Matcha Coffee",
        priceBaht = "100.00",
        //priceSat = "26,000",
        count = 1u
    )
}

