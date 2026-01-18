package org.siamdev.zappos.ui.screens.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.MaterialButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import org.siamdev.zappos.ui.components.GlowEffects
import org.siamdev.zappos.ui.components.GlowShape
import org.siamdev.zappos.ui.components.NavigationDrawer
import org.siamdev.zappos.ui.components.TopBar


@Composable
fun CounterScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToMenu: () -> Unit = {},
    onNavigateToSetting: () -> Unit = {}
) {
    var count by remember { mutableIntStateOf(0) }
    var isDrawerOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Top Bar
            TopBar(
                title = "Counter",
                modifier = Modifier,
                onSegmentClick = { isDrawerOpen = true }
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 48.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row {
                    MaterialButton(
                        modifier = Modifier.size(50.dp),
                        iconCenter = Icons.Default.Remove,
                        iconSize = 28,
                        onClick = { count-- }
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    MaterialButton(
                        modifier = Modifier.size(50.dp),
                        iconCenter = Icons.Default.Add,
                        iconSize = 28,
                        onClick = { count++ }
                    )
                }

                Spacer(Modifier.height(20.dp))

                MaterialButton(
                    modifier = Modifier.width(117.dp),
                    text = "reset count",
                    onClick = { count = 0 }
                )
            }
        }

        // Navigation Drawer
        NavigationDrawer(
            isOpen = isDrawerOpen,
            onDismiss = { isDrawerOpen = false },
            onNavigateToHome = onNavigateToHome,
            onNavigateToMenu = onNavigateToMenu,
            onNavigateToSetting = onNavigateToSetting
        )
    }
}


@Preview
@Composable
fun CounterScreenPreview() {
    CounterScreen()
}