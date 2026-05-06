/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit
import org.jetbrains.compose.resources.painterResource
import org.siamdev.zappos.theme.YellowPrimary
import zappos.composeapp.generated.resources.compose_multiplatform


enum class MenuViewMode { LIST, GRID }

@Composable
fun MenuItemCard(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String? = null,
    category: String = "",
    isRecommended: Boolean = false,
    isAvailable: Boolean = true,
    count: UInt = 0u,
    viewMode: MenuViewMode = MenuViewMode.LIST,
    showChevron: Boolean = false,
    onAddClick: () -> Unit = {},
    onReduceClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    when (viewMode) {
        MenuViewMode.LIST -> MenuItemCardList(
            imageUrl, name, priceBaht, priceSat, category, isRecommended, isAvailable,
            count, showChevron, onAddClick, onClick
        )
        MenuViewMode.GRID -> MenuItemCardGrid(
            imageUrl, name, priceBaht, priceSat, category, isRecommended, isAvailable,
            count, onAddClick
        )
    }
}


@Composable
private fun MenuItemCardList(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String?,
    category: String,
    isRecommended: Boolean,
    isAvailable: Boolean,
    count: UInt,
    showChevron: Boolean,
    onAddClick: () -> Unit,
    onClick: () -> Unit
) {
    val fontScale = LocalDensity.current.fontScale
    val imageSize = (72f * fontScale.coerceIn(1f, 1.5f)).dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(if (showChevron) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            MenuImage(imageUrl = imageUrl, size = imageSize, cornerRadius = 10.dp)

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (isRecommended) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = YellowPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                if (category.isNotBlank()) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PriceRow(priceBaht = priceBaht, priceSat = priceSat)
                    if (!isAvailable) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "Unavailable",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(4.dp))

            if (showChevron) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.CenterVertically)
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    if (count > 0u) {
                        CountBadge(count = count)
                        Spacer(Modifier.height(6.dp))
                    }
                    AddButton(onClick = onAddClick, size = 36.dp)
                }
            }
        }
    }
}



@Composable
private fun MenuItemCardGrid(
    imageUrl: String,
    name: String,
    priceBaht: String,
    priceSat: String?,
    category: String,
    isRecommended: Boolean,
    isAvailable: Boolean,
    count: UInt,
    onAddClick: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val cardWidth = maxWidth

        val scale = (cardWidth / 160.dp).coerceIn(0.85f, 1.25f)

        val imageHeight = 170.dp * scale
        val padding = 10.dp * scale
        val titleSize = MaterialTheme.typography.bodyLarge.fontSize * scale
        val smallSize = MaterialTheme.typography.bodySmall.fontSize * scale
        val iconSize = 15.dp * scale
        val buttonSize = 32.dp * scale

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(0.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column {
                MenuImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .height(imageHeight)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                    cornerRadius = 0.dp
                )

                Column(Modifier.padding(padding)) {

                    // Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name,
                            fontSize = titleSize,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, false)
                        )

                        if (isRecommended) {
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = YellowPrimary,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    }

                    if (category.isNotBlank()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = category,
                            fontSize = smallSize,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    Spacer(Modifier.height(4.dp * scale))

                    PriceRow(
                        priceBaht = priceBaht,
                        priceSat = priceSat
                    )

                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .alpha(if (isAvailable) 0f else 1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "Unavailable",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            maxLines = 1
                        )
                    }

                    Spacer(Modifier.height(8.dp * scale))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (count > 0u) {
                            CountBadge(count = count)
                        } else {
                            Spacer(Modifier.weight(1f))
                        }

                        AddButton(
                            onClick = onAddClick,
                            size = buttonSize
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun MenuImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp? = null,
    cornerRadius: androidx.compose.ui.unit.Dp = 10.dp,
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
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = contentScale,
        placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
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
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (priceSat != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.sat_unit),
                    contentDescription = null,
                    tint = YellowPrimary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = priceSat,
                    style = MaterialTheme.typography.bodySmall,
                    color = YellowPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = "×$count",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
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



// LIST — count + category + recommended
@Preview
@Composable
fun MenuItemCardListPreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg",
        name = "Mocha",
        priceBaht = "70.00",
        priceSat = "17,500",
        category = "Beverages · Coffee",
        isRecommended = true,
        count = 2u,
        viewMode = MenuViewMode.LIST
    )
}

// LIST — no count, unavailable
@Preview
@Composable
fun MenuItemCardListUnavailablePreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg",
        name = "Latte",
        priceBaht = "70.00",
        priceSat = "17,500",
        category = "Beverages · Coffee",
        isAvailable = false,
        viewMode = MenuViewMode.LIST
    )
}

// LIST — chevron mode (ProductListDetailScreen usage)
@Preview
@Composable
fun MenuItemCardChevronPreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg",
        name = "Matcha Latte",
        priceBaht = "100.00",
        category = "Beverages · Tea",
        isRecommended = true,
        showChevron = true,
        viewMode = MenuViewMode.LIST
    )
}

// GRID — recommended, with count
@Preview
@Composable
fun MenuItemCardGridPreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
        name = "Matcha Coffee",
        priceBaht = "100.00",
        priceSat = "26,000",
        category = "Matcha",
        isRecommended = true,
        count = 1u,
        viewMode = MenuViewMode.GRID
    )
}



// GRID — unavailable, no count
@Preview
@Composable
fun MenuItemCardGridUnavailablePreview() {
    MenuItemCard(
        imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
        name = "Milk",
        priceBaht = "50.00",
        priceSat = "12,500",
        category = "Other",
        isAvailable = false,
        viewMode = MenuViewMode.GRID
    )
}