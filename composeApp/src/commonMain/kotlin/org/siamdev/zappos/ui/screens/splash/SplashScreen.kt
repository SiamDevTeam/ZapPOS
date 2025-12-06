/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
import org.jetbrains.compose.resources.painterResource
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.ic_app_logo
import zappos.composeapp.generated.resources.siamdev_logo
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2

@Composable
fun SplashScreen(
    viewModel: SplashViewModel/* viewModel() หรือรับผ่าน DI */,
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

/*
@Preview
@Composable
fun SplashScreenPreview()  {
    SplashScreen()
}
*/
