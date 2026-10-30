package com.playverse.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.playverse.app.data.Catalog
import com.playverse.app.data.Player
import com.playverse.app.ui.components.PopupHost
import com.playverse.app.ui.home.HomePopup
import com.playverse.app.ui.home.HomeScreen
import com.playverse.app.ui.home.Tab
import com.playverse.app.ui.popups.RenamePopup

sealed interface Screen {
    data object Splash : Screen
    data object Home : Screen
    data object Account : Screen
    data class Game(val id: String) : Screen
}

@Composable
fun PlayverseApp(player: Player) {
    var screen by remember { mutableStateOf<Screen>(Screen.Splash) }
    var tab by remember { mutableStateOf(Tab.Games) }
    var homePopup by remember { mutableStateOf<HomePopup?>(null) }
    var renaming by remember { mutableStateOf(false) }

    AnimatedContent(screen, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "screen") { current ->
        when (current) {
            Screen.Splash -> SplashScreen(onDone = {
                // First launch: welcome bonus, then today's daily reward.
                homePopup = when {
                    !player.welcomeClaimed -> HomePopup.Welcome
                    !player.claimedDailyToday -> HomePopup.Daily
                    else -> null
                }
                screen = Screen.Home
            })
            Screen.Home -> HomeScreen(
                player,
                tab,
                onTab = { tab = it },
                popup = homePopup,
                onPopup = { homePopup = it },
                onPlay = { screen = Screen.Game(it.id) },
                onAccount = { screen = Screen.Account },
            )
            Screen.Account -> Box {
                BackHandler { screen = Screen.Home }
                AccountScreen(player, onBack = { screen = Screen.Home }, onRename = { renaming = true })
                PopupHost(if (renaming) Unit else null, onDismiss = { renaming = false }) {
                    RenamePopup(player) { renaming = false }
                }
            }
            is Screen.Game -> {
                BackHandler { screen = Screen.Home }
                GameScreen(player, Catalog.game(current.id), onExit = { screen = Screen.Home })
            }
        }
    }
}
