package org.siamdev.zappos.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.MaterialButton
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2

@Composable
fun LoginScreen(
    onLoginNostr: () -> Unit = {},
    onLoginAnonymous: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isWide = maxWidth >= 600.dp
        val cardWidth = if (isWide) 420.dp else maxWidth

        // background glow บนซ้าย
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-80).dp, y = (-80).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            YellowPrimary.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(50)
                )
        )


        // glow ขวาล่าง — เพิ่มใหม่ให้สมดุล
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            YellowPrimary.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card หลัก
            Column(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(horizontal = if (isWide) 0.dp else 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 28.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(Res.drawable.zappos_dark_horizontal_v2),
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