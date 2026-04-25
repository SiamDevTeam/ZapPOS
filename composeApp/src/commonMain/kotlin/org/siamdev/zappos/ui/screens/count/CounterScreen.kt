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
package org.siamdev.zappos.ui.screens.count

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.MaterialButton
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import org.siamdev.zappos.LocalCounterVM
import org.siamdev.zappos.ui.components.WorkspaceHeader

@Composable
fun CounterScreen(
    onOpenDrawer: () -> Unit = {}
) {

    val viewModel = LocalCounterVM.current
    val count by viewModel.count.collectAsState()

    CounterContent(
        count = count,
        onPlus = { viewModel.plus() },
        onMinus = { viewModel.minus() },
        onReset = { viewModel.reset() },
        onOpenDrawer = onOpenDrawer
    )
}

@Composable
fun CounterContent(
    count: Int,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
    onReset: () -> Unit,
    onOpenDrawer: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            WorkspaceHeader(title = "Counter", onSegmentClick = onOpenDrawer)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row {
                    MaterialButton(
                        modifier = Modifier.size(50.dp),
                        iconCenter = Icons.Default.Remove,
                        iconSize = 28,
                        onClick = onMinus
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    MaterialButton(
                        modifier = Modifier.size(50.dp),
                        iconCenter = Icons.Default.Add,
                        iconSize = 28,
                        onClick = onPlus
                    )
                }
                Spacer(Modifier.height(20.dp))
                MaterialButton(
                    modifier = Modifier.width(117.dp),
                    text = "reset count",
                    onClick = onReset
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun CounterScreenPreview() {
    CounterContent(
        count = 42,
        onPlus = {},
        onMinus = {},
        onReset = {}
    )
}