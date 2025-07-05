package org.siamdev.zappos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.view.MaterialButton
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.ic_app_logo

@Composable
fun LoginScreen(onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_app_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(180.dp)
            )
            MaterialButton(
                text = "Login with Nostr account",
                onClick = onButtonClick
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onButtonClick = {})
}
