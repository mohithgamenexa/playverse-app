package com.playverse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.playverse.app.data.Player
import com.playverse.app.ui.PlayverseApp
import com.playverse.app.ui.theme.PlayverseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val player = Player(applicationContext)
        setContent {
            PlayverseTheme { PlayverseApp(player) }
        }
    }
}
