package org.siamdev.zappos.ui.screens.demo

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable


@Composable
fun CounterScreen() {
    var count by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {


        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Count: $count", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                MaterialButton(
                    modifier = Modifier.size(50.dp),
                    iconCenter = Icons.Default.Remove,
                    iconSize = 28,
                    onClick = { count-- }
                )
                Spacer(modifier = Modifier.width(16.dp))

                MaterialButton(
                    modifier = Modifier.size(50.dp),
                    iconCenter = Icons.Default.Add,
                    iconSize = 28,
                    onClick = { count++ }
                )
            }
            Spacer(Modifier.height(20.dp))

            MaterialButton(
                modifier = Modifier.width(117.dp),
                text = "reset count",
                onClick = { count = 0 }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    CounterScreen()
}
