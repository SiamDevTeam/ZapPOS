/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.progress

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.siamdev.zappos.LocalProgressVM
import org.siamdev.zappos.theme.YellowPrimary

private val DotContainerSize = 22.dp
private val DotActiveSize = 20.dp
private val DotIdleSize = 16.dp
private val LineHeight = 2.dp

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier
) {
    val vm = LocalProgressVM.current
    val steps = vm.steps
    val currentStep = vm.currentStep

    if (steps.isEmpty()) return

    val animatedStep by animateFloatAsState(
        targetValue = currentStep.toFloat(),
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progressStep"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        steps.forEachIndexed { index, label ->

            val isDone = index < currentStep
            val isActive = index == currentStep

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProgressLine(
                        modifier = Modifier.weight(1f),
                        fillProgress = if (index > 0) {
                            (animatedStep - (index - 1)).coerceIn(0f, 1f)
                        } else {
                            0f
                        }
                    )

                    ProgressDot(
                        isDone = isDone,
                        isActive = isActive,
                        index = index
                    )

                    ProgressLine(
                        modifier = Modifier.weight(1f),
                        fillProgress = if (index < steps.lastIndex) {
                            (animatedStep - index).coerceIn(0f, 1f)
                        } else {
                            0f
                        }
                    )
                }

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontWeight = if (isActive) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
                    color = when {
                        isActive -> MaterialTheme.colorScheme.onSurface
                        isDone -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ProgressLine(
    modifier: Modifier = Modifier,
    fillProgress: Float
) {
    Box(
        modifier = modifier.height(LineHeight)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fillProgress)
                .background(YellowPrimary)
        )
    }
}

@Composable
private fun ProgressDot(
    isDone: Boolean,
    isActive: Boolean,
    index: Int
) {

    val dotSize by animateDpAsState(
        targetValue = if (isActive) {
            DotActiveSize
        } else {
            DotIdleSize
        },
        animationSpec = tween(300),
        label = "dotSize"
    )

    val dotBackgroundColor by animateColorAsState(
        targetValue = if (isDone || isActive) {
            YellowPrimary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
        },
        animationSpec = tween(300),
        label = "dotBackground"
    )

    Box(
        modifier = Modifier.size(DotContainerSize),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(dotSize)
                .clip(CircleShape)
                .background(dotBackgroundColor),
            contentAlignment = Alignment.Center
        ) {

            Crossfade(
                targetState = when {
                    isDone -> DotState.Done
                    isActive -> DotState.Active
                    else -> DotState.Idle
                },
                animationSpec = tween(200),
                label = "dotState"
            ) { state ->

                when (state) {

                    DotState.Done -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }

                    DotState.Active -> {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }

                    DotState.Idle -> {
                        Spacer(modifier = Modifier.width(0.dp))
                    }
                }
            }
        }
    }
}

private enum class DotState {
    Done,
    Active,
    Idle
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun ProgressBarPreview() {

    val steps = listOf(
        "Confirm",
        "Checkout",
        "Payment",
        "Successful"
    )

    MaterialTheme {

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            steps.indices.forEach { step ->

                val vm = remember(step) {
                    ProgressViewModel().also {
                        it.setup(steps, step)
                    }
                }

                CompositionLocalProvider(
                    LocalProgressVM provides vm
                ) {
                    ProgressBar()
                }
            }
        }
    }
}