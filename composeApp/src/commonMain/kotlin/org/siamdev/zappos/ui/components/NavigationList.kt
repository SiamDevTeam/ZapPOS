package org.siamdev.zappos.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2

@Composable
fun NavigationList(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToCounter: () -> Unit = {},
    onNavigateToMenu: () -> Unit = {},
    onNavigateToGlow: () -> Unit = {},
    onNavigateToSetting: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() }
        )
    }

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)),
        exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 20.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(YellowPrimary.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(Res.drawable.zappos_dark_horizontal_v2),
                            contentDescription = "ZapPOS Logo",
                            modifier = Modifier
                                .height(36.dp)
                                .wrapContentWidth(),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(
                            color = YellowPrimary.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
                            )
                            Text(
                                text = "Point of Sale System",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MENU",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )

                // เมนูหลัก scroll ได้
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        DrawerNavigationItem(
                            icon = Icons.Default.Home,
                            title = "Home",
                            onClick = { onNavigateToHome(); onDismiss() }
                        )
                    }
                    item {
                        DrawerNavigationItem(
                            icon = Icons.Default.Restaurant,
                            title = "Main Menu",
                            onClick = { onNavigateToMenu(); onDismiss() }
                        )
                    }
                    item {
                        DrawerNavigationItem(
                            icon = Icons.Default.Add,
                            title = "Counter",
                            onClick = { onNavigateToCounter(); onDismiss() }
                        )
                    }
                    item {
                        DrawerNavigationItem(
                            icon = Icons.Default.AutoAwesome,
                            title = "Glow Effects",
                            onClick = { onNavigateToGlow(); onDismiss() }
                        )
                    }
                }

                // Settings ติดด้านล่างเสมอ
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                DrawerNavigationItem(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    onClick = { onNavigateToSetting(); onDismiss() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DrawerNavigationItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(YellowPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = YellowPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(
    name = "Pixel 6",
    showBackground = true,
    widthDp = 411,
    heightDp = 891
)
@Composable
private fun NavigationDrawerPreview() {
    MaterialTheme {
        NavigationList(
            isOpen = true,
            onDismiss = {}
        )
    }
}