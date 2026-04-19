/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit
import org.jetbrains.compose.resources.painterResource
import zappos.composeapp.generated.resources.compose_multiplatform


enum class MenuViewMode { LIST, GRID }

@Composable
fun MenuItemCard(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String? = null,
    count: UInt = 0u,
    viewMode: MenuViewMode = MenuViewMode.LIST,
    onAddClick: () -> Unit = {},
    onReduceClick: () -> Unit = {}
) {
    when (viewMode) {
        MenuViewMode.LIST -> MenuItemCardList(
            imageUrl, name, priceBaht, priceSat, count, onAddClick
        )
        MenuViewMode.GRID -> MenuItemCardGrid(
            imageUrl, name, priceBaht, priceSat, count, onAddClick
        )
    }
}

// ─── LIST (แนวนอน) ───────────────────────────
@Composable
private fun MenuItemCardList(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String?,
    count: UInt,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuImage(imageUrl = imageUrl, size = 72.dp, cornerRadius = 12.dp)

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))
                PriceRow(priceBaht = priceBaht, priceSat = priceSat)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (count > 0u) {
                    CountBadge(count = count)
                    Spacer(Modifier.height(6.dp))
                }
                AddButton(onClick = onAddClick, size = 36.dp)
            }

            Spacer(Modifier.width(4.dp))
        }
    }
}

// ─── GRID (แนวตั้ง) ──────────────────────────
@Composable
private fun MenuItemCardGrid(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String?,
    count: UInt,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image เต็มบน
            MenuImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                cornerRadius = 0.dp,
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                PriceRow(priceBaht = priceBaht, priceSat = priceSat)
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (count > 0u) CountBadge(count = count)
                    else Spacer(Modifier.weight(1f))
                    AddButton(onClick = onAddClick, size = 34.dp)
                }
            }
        }
    }
}

// ─── Shared subcomponents ─────────────────────

@Composable
private fun MenuImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp? = null,
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp,
    contentScale: ContentScale = ContentScale.Crop
) {
    val baseModifier = if (size != null) modifier.size(size) else modifier
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(imageUrl)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = null,
        modifier = baseModifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(0xFFF5F5F5)),
        contentScale = contentScale,
        placeholder = ColorPainter(Color(0xFFF5F5F5)),
        error = painterResource(Res.drawable.compose_multiplatform)
    )
}

@Composable
private fun PriceRow(priceBaht: String, priceSat: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CurrencyLira,
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = priceBaht,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        if (priceSat != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.sat_unit),
                    contentDescription = null,
                    tint = Color(0xFFFFB700),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = priceSat,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFB700)
                )
            }
        }
    }
}

@Composable
private fun CountBadge(count: UInt) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = "×$count",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AddButton(
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp
) {
    MaterialButton(
        modifier = Modifier.size(size),
        iconCenter = Icons.Default.Add,
        iconColor = Color.White,
        buttonColor = Color(0xFF070E1E),
        onClick = onClick
    )
}

// ─── Previews ────────────────────────────────

@Preview
@Composable
fun MenuItemCardListPreview() {
    MenuItemCard(
        imageUrl = "",
        name = "Mocha",
        priceBaht = "70.00",
        priceSat = "17,500",
        count = 2u,
        viewMode = MenuViewMode.LIST
    )
}

@Preview
@Composable
fun MenuItemCardGridPreview() {
    MenuItemCard(
        imageUrl = "",
        name = "Matcha Latte",
        priceBaht = "100.00",
        priceSat = "26,000",
        count = 1u,
        viewMode = MenuViewMode.GRID
    )
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

