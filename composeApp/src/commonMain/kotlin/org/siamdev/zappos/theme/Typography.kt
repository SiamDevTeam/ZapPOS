package org.siamdev.zappos.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

val AppTypography = Typography(
    bodyLarge = Typography().bodyLarge.copy(
        fontFamily = FontFamily.Default
    ),
    labelLarge = Typography().labelLarge.copy(
        fontFamily = FontFamily.Default
    )
)