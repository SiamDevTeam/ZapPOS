/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.common.GlassCard
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.utils.LockOrientation
import org.siamdev.zappos.utils.Orientation
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2
import zappos.composeapp.generated.resources.zappos_white_horizontal_v2

@Composable
fun LoginScreen(
    onLoginNostr: () -> Unit = {},
    onLoginAnonymous: () -> Unit = {}
) {
    LockOrientation(Orientation.PORTRAIT)
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth >= 600.dp
        val cardWidth = if (isWide) 420.dp else maxWidth

        // Capture theme colors before entering Canvas (no composable context inside)
        val bgColor      = MaterialTheme.colorScheme.background
        val primaryGlow  = MaterialTheme.colorScheme.primary
        val secondaryGlow = MaterialTheme.colorScheme.secondary

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(bgColor)
            // Primary glow — upper left
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(primaryGlow.copy(alpha = 0.45f), Color.Transparent),
                    center = Offset(size.width * 0.15f, size.height * 0.12f),
                    radius = size.minDimension * 0.72f
                )
            )
            // Secondary glow — upper right
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(secondaryGlow.copy(alpha = 0.4f), Color.Transparent),
                    center = Offset(size.width * 0.88f, size.height * 0.08f),
                    radius = size.minDimension * 0.65f
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card หลัก
            GlassCard(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(horizontal = if (isWide) 0.dp else 24.dp)
                    .padding(horizontal = 28.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
                val logoRes = if (isDark) Res.drawable.zappos_white_horizontal_v2 else Res.drawable.zappos_dark_horizontal_v2
                Image(
                    painter = painterResource(logoRes),
                    contentDescription = "ZapPOS Logo",
                    modifier = Modifier
                        .height(48.dp)
                        .wrapContentWidth(),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Point of Sale System",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Headline
                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Choose how you want to sign in",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Nostr Login — primary
                MaterialButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Sign in with Nostr",
                    iconStart = Icons.Default.Key,
                    iconEnd = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    onClick = onLoginNostr
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Anonymous Login — secondary
                MaterialButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue as Guest",
                    iconStart = Icons.Default.Person,
                    iconEnd = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    buttonColor = Color.Transparent,
                    showBorder = true,
                    onClick = onLoginAnonymous
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom link
            Text(
                text = "Don't have an account?  Create Nostr account",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}