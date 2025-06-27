package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.view.MenuItemView

@Composable
fun MenuScreen(
    menuList: List<MenuItem>,
    onMenuItemClick: (MenuItem) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            menuList.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { item ->
                        Box(modifier = Modifier.weight(1f)) {
                            MenuItemView(
                                name = item.name,
                                priceSats = item.priceSats,
                                priceBaht = item.priceBaht
                            ) {
                                onMenuItemClick(item)
                            }
                        }
                    }
                    // Add empty box if row has only one item
                    if (rowItems.size == 1) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
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
