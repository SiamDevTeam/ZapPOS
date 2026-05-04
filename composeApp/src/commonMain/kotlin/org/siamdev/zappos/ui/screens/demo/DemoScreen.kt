/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.WorkspaceHeader

@Composable
fun DemoScreen(onOpenDrawer: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {

            WorkspaceHeader(
                title = "Home",
                onSegmentClick = onOpenDrawer
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Demo Screen",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(32.dp))

            }
        }

    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun DemoScreenPreview() {
    DemoScreen()
}