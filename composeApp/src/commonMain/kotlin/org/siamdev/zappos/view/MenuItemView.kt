package org.siamdev.zappos.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.MenuItem

@Composable
fun MenuItemView(
    item: MenuItem,
    onClick: (() -> Unit)? = null
) {
    var isImageError by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
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
            if (item.imageUrl != null && !isImageError) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(72.dp)
                        .padding(bottom = 4.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                    onError = { isImageError = true }
                )
            } else {
                placeHolderView(firstChar = item.name[0].uppercase())
            }
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = item.priceSats,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = item.priceBaht,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp).align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun placeHolderView(firstChar: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(72.dp)
            .background(Color.LightGray, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = firstChar,
            fontSize = 16.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun previewMenuItem() {
    MenuItemView(
        item = MenuItem(
            name = "Americano",
            priceSats = "60 Sats",
            priceBaht = "฿60",
            imageUrl = null
        )
    )
}

@Preview
@Composable
fun previewMenuItemWithImageUrl() {
    MenuItemView(
        item = MenuItem(
            name = "Americano",
            priceSats = "60 Sats",
            priceBaht = "฿60",
            imageUrl = "https://images.pexels.com/photos/3704460/pexels-photo-3704460.jpeg"
        )
    )
}
