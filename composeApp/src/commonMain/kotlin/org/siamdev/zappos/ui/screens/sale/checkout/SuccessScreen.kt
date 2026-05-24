/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.LocalProgressVM
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.progress.ProgressBar
import org.siamdev.zappos.ui.screens.sale.SaleOrderSteps
import org.siamdev.zappos.ui.components.progress.ProgressViewModel

private val GreenSuccess = Color(0xFF4CAF50)

@Composable
fun SuccessScreen(onOpen: () -> Unit = {}) {
    val progressVM = LocalProgressVM.current
    SideEffect { progressVM.setup(SaleOrderSteps, SaleOrderSteps.lastIndex) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Spacer(Modifier.height(8.dp))
        ProgressBar()

        // Center content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Glow ring + icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(GreenSuccess.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(GreenSuccess.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint               = GreenSuccess,
                        modifier           = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text       = "Payment Successful",
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text  = "Your order has been placed.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        // Action button
        MaterialButton(
            modifier    = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text        = "to Menu Screen",
            buttonColor = MaterialTheme.colorScheme.primary,
            onClick     = onOpen
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun SuccessScreenPreview() {
    val progressVM = remember { ProgressViewModel() }
    CompositionLocalProvider(LocalProgressVM provides progressVM) {
        MaterialTheme { SuccessScreen() }
    }
}
