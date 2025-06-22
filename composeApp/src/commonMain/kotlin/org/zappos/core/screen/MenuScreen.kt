package org.zappos.core.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.zappos.core.view.MenuItemView

@Composable
fun MenuScreen(
    menuList: List<MenuItem>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
