/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.ic_app_logo
import zappos.composeapp.generated.resources.siamdev_logo
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onSplashFinished: () -> Unit = {}
) {

    val ready = viewModel.isReady.collectAsState()
    LaunchedEffect(ready.value) {
        if (ready.value) {
            onSplashFinished()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        val isWide = maxWidth >= 500.dp

        val logoRes = if (isWide) {
            Res.drawable.zappos_dark_horizontal_v2
        } else {
            Res.drawable.ic_app_logo
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            val logoModifier = if (isWide) {
                Modifier.width(250.dp)
            } else {
                Modifier.size(180.dp)
            }

            Image(
                painter = painterResource(logoRes),
                contentDescription = null,
                modifier = logoModifier
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (isWide) 35.dp else 45.dp)
        ) {
            Text(
                text = "from",
                color = Color(0xFF515151),
                fontSize = if (isWide) 14.sp else 12.sp
            )
            Image(
                painter = painterResource(Res.drawable.siamdev_logo),
                contentDescription = "Logo",
                modifier = Modifier.height(if (isWide) 35.dp else 32.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun SplashScreenPreview() {
    val fakeViewModel: SplashViewModel = viewModel()
    SplashScreen(
        viewModel = fakeViewModel,
        onSplashFinished = {}
    )
}

