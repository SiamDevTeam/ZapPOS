package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.view.MenuItemView

@Composable
fun MenuScreen(
    menuList: List<MenuItem>
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            menuList.forEach { items ->
                item {
                    MenuItemView(
                        name = items.name,
                        priceSats = items.priceSats,
                        priceBaht = items.priceBaht
                    ) {
                        // Handle item click if needed
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    MenuScreen(
        menuList = listOf(
            MenuItem("Item 1", "100 sat", "3.00 baht", "https://example.com/image1.jpg"),
            MenuItem("Item 2", "200 sat", "6.00 baht", "https://example.com/image2.jpg"),
            MenuItem("Item 3", "300 sat", "9.00 baht", "https://example.com/image3.jpg"),
            MenuItem("Item 4", "400 sat", "12.00 baht", "https://example.com/image4.jpg")
        )
    )
}

// TODO: will move
class MenuItem(
    val name: String,
    val priceSats: String,
    val priceBaht: String,
    val imageUrl: String
)
