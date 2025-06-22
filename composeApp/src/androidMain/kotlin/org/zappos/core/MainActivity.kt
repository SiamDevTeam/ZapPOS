package org.zappos.core

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import org.zappos.core.screen.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            // if not login
            LoginScreen(
                onButtonClick = {
                    context.startActivity(Intent(context, PosActivity::class.java))
                }
            )
            // if login, go to PosActivity
        }
    }
}
