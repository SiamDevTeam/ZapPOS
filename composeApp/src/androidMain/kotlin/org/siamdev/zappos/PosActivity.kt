package org.siamdev.zappos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.screen.CheckoutScreen
import org.siamdev.zappos.screen.HistoryScreen
import org.siamdev.zappos.screen.MenuItem
import org.siamdev.zappos.screen.MenuScreen
import org.siamdev.zappos.screen.TransactionHistory

class PosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewScreen(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Menu",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            navigationIcon = {
                IconButton(onClick = {
                    if (drawerState.isClosed) {
                        scope.launch {
                            drawerState.open()
                        }
                    } else {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = null)
                }
            },
        )
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                    .requiredWidth(200.dp)
                    .fillMaxHeight()
                ) {
                    NavigationDrawerItem(
                        label = { Text(text = "Menu") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("menu") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Transaction History") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("history") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        thickness = 1.dp
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Logout") },
                        selected = false,
                        onClick = {
                            // back to login screen
                            context.startActivity(Intent(context, MainActivity::class.java))
                            if (context is ComponentActivity) {
                                context.finish()
                            }
                        }
                    )
                }
            },
            content = {
                NavHost(
                    navController = navController,
                    startDestination = "menu"
                ) {
                    composable("menu") { MainMenuScreen() }
                    composable("history") { HistoryScreen() }
                }
            }
        )
    }
}

@Composable
fun MainMenuScreen() {
    Column {
        MenuScreen(
            menuList = listOf(
                MenuItem("Item 1", "100 sat", "3.00 baht", "https://example.com/image1.jpg"),
                MenuItem("Item 2", "200 sat", "6.00 baht", "https://example.com/image2.jpg"),
                MenuItem("Item 3", "300 sat", "9.00 baht", "https://example.com/image3.jpg"),
                MenuItem("Item 4", "400 sat", "12.00 baht", "https://example.com/image4.jpg")
            )
        )
        CheckoutScreen(
            checkoutList = emptyList(),
            onAddItemClick = {},
            onRemoveItemClick = {},
            onDeleteItemClick = {}
        )
    }
}

@Composable
fun HistoryScreen() {
    HistoryScreen(
        totalSats = "10000 sats",
        totalBaht = "about ฿21.00",
        historyList = listOf(
            TransactionHistory(
                transactionId = "1",
                name = "Transaction 1",
                date = "June 1, 2025",
                sats = "1000 sats",
                baht = "฿20.00"
            ),
            TransactionHistory(
                transactionId = "2",
                name = "Transaction 2",
                date = "June 2, 2025",
                sats = "2000 sats",
                baht = "฿40.00"
            ),
            TransactionHistory(
                transactionId = "3",
                name = "Transaction 3",
                date = "June 3, 2025",
                sats = "3000 sats",
                baht = "฿60.00"
            )
        ),
        onItemClick = {}
    )
}
