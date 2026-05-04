/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.WorkspaceHeader

@Composable
fun TopBarScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            WorkspaceHeader()
            WorkspaceHeader(title = "ZapPOS")
            WorkspaceHeader(title = "Demo")
            WorkspaceHeader(title = "Zap POS")
            WorkspaceHeader(title = "rushmi0")
            WorkspaceHeader(title = "lnwza007")
            WorkspaceHeader(title = "minseo")
            WorkspaceHeader(title = "Vaz")
            WorkspaceHeader(title = "Emperor13")
        }

    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun TopBarScreenPreview() {
    TopBarScreen()
}
