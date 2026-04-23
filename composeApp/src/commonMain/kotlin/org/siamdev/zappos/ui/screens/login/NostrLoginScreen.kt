package org.siamdev.zappos.ui.screens.login

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.MaterialButton
import org.siamdev.zappos.utils.LockOrientation
import org.siamdev.zappos.utils.Orientation


private enum class NostrLoginTab { NSEC, MNEMONIC }



@Composable
fun NostrLoginScreen(
    onBack: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    LockOrientation(Orientation.PORTRAIT)
    var selectedTab by remember { mutableStateOf(NostrLoginTab.NSEC) }

    // nsec state
    var nsecValue by remember { mutableStateOf("") }
    var nsecVisible by remember { mutableStateOf(false) }
    var nsecError by remember { mutableStateOf<String?>(null) }

    // mnemonic state
    var mnemonicValue by remember { mutableStateOf("") }
    var mnemonicError by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isWide = maxWidth >= 600.dp
        val cardWidth = if (isWide) 420.dp else maxWidth

        // Glow บนซ้าย
        Box(
            modifier = Modifier
                .size(500.dp)
                .offset(x = (-150).dp, y = (-150).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(YellowPrimary.copy(alpha = 0.22f), Color.Transparent)
                    )
                )
        )

        // Glow ขวาล่าง
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(YellowPrimary.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(horizontal = if (isWide) 0.dp else 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 28.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Back + Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Sign in with Nostr",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Your key never leaves this device",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tab Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    NostrLoginTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) YellowPrimary
                                    else Color.Transparent
                                )
                                .then(
                                    Modifier.padding(vertical = 10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(
                                onClick = {
                                    selectedTab = tab
                                    nsecError = null
                                    mnemonicError = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = when (tab) {
                                        NostrLoginTab.NSEC -> "Private Key"
                                        NostrLoginTab.MNEMONIC -> "Seed Phrase"
                                    },
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF515151)
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Input area — animate เมื่อสลับ tab
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    }
                ) { tab ->
                    when (tab) {

                        NostrLoginTab.NSEC -> {
                            Column {
                                Text(
                                    text = "nsec Private Key",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = nsecValue,
                                    onValueChange = {
                                        nsecValue = it
                                        nsecError = null
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text(
                                            "nsec1...",
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                    },
                                    visualTransformation = if (nsecVisible) VisualTransformation.None
                                    else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = { nsecVisible = !nsecVisible }) {
                                            Icon(
                                                imageVector = if (nsecVisible) Icons.Default.VisibilityOff
                                                else Icons.Default.Visibility,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                            )
                                        }
                                    },
                                    isError = nsecError != null,
                                    supportingText = nsecError?.let { err ->
                                        { Text(err, color = MaterialTheme.colorScheme.error) }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                            }
                        }

                        NostrLoginTab.MNEMONIC -> {
                            Column {
                                Text(
                                    text = "Mnemonic Seed Phrase",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = mnemonicValue,
                                    onValueChange = {
                                        mnemonicValue = it
                                        mnemonicError = null
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    placeholder = {
                                        Text(
                                            "word1 word2 word3 ...",
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                    },
                                    isError = mnemonicError != null,
                                    supportingText = mnemonicError?.let { err ->
                                        { Text(err, color = MaterialTheme.colorScheme.error) }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    maxLines = 4
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Enter 12 or 24 words separated by spaces",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                MaterialButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Sign In",
                    iconStart = Icons.Default.Key,
                    onClick = {
                        when (selectedTab) {
                            NostrLoginTab.NSEC -> {
                                if (nsecValue.isBlank()) {
                                    nsecError = "Please enter your private key"
                                } else if (!nsecValue.startsWith("nsec1")) {
                                    nsecError = "Key must start with nsec1..."
                                } else {
                                    onLoginSuccess()
                                }
                            }
                            NostrLoginTab.MNEMONIC -> {
                                val words = mnemonicValue.trim().split(" ").filter { it.isNotBlank() }
                                if (words.isEmpty()) {
                                    mnemonicError = "Please enter your seed phrase"
                                } else if (words.size != 12 && words.size != 24) {
                                    mnemonicError = "Seed phrase must be 12 or 24 words (got ${words.size})"
                                } else {
                                    onLoginSuccess()
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Warning note
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = YellowPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Your private key is stored locally and never sent to any server.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
fun NostrLoginScreenPreview() {
    NostrLoginScreen()
}