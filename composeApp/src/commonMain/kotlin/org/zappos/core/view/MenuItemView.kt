package org.zappos.core.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.compose_multiplatform

@Composable
fun MenuItemView(
    name: String,
    priceSats: String,
    priceBaht: String,
    imageRes: Int? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .clickable { onClick?.invoke() },
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(72.dp)
                        .padding(bottom = 4.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(72.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = priceSats,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = priceBaht,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp).align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun previewMenuItem() {
    MenuItemView(
        name = "Americano",
        priceSats = "60 Sats",
        priceBaht = "฿60",
        imageRes = null
    )
}
