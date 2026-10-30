package com.playverse.app.ui.home

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.playverse.app.R
import com.playverse.app.data.Catalog
import com.playverse.app.data.Game
import com.playverse.app.data.Player
import com.playverse.app.data.Task
import com.playverse.app.data.TaskAction
import com.playverse.app.ui.components.PopupHost
import com.playverse.app.ui.components.RoomBackdrop
import com.playverse.app.ui.components.toast
import com.playverse.app.ui.popups.DailyRewardsPopup
import com.playverse.app.ui.popups.ProfilePopup
import com.playverse.app.ui.popups.RenamePopup
import com.playverse.app.ui.popups.SettingsPopup
import com.playverse.app.ui.popups.WelcomeBonusPopup
import com.playverse.app.ui.theme.PV

enum class HomePopup { Welcome, Daily, Settings, Profile, Rename }

@Composable
fun HomeScreen(
    player: Player,
    tab: Tab,
    onTab: (Tab) -> Unit,
    popup: HomePopup?,
    onPopup: (HomePopup?) -> Unit,
    onPlay: (Game) -> Unit,
    onAccount: () -> Unit,
) {
    val context = LocalContext.current
    val invite = {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Come play with me on Playverse!")
        }
        context.startActivity(Intent.createChooser(send, "Invite friends"))
    }
    val watchAd = { context.toast("Rewarded ads come in a later step") }
    val onTask = { task: Task ->
        when (task.action) {
            TaskAction.Play -> task.gameId?.let { onPlay(Catalog.game(it)) }
            TaskAction.WatchAd -> watchAd()
            TaskAction.Invite -> invite()
        }
        Unit
    }

    Box(Modifier.fillMaxSize().background(PV.Cream)) {
        RoomBackdrop(Modifier.fillMaxWidth().height(360.dp))
        Column(Modifier.fillMaxSize()) {
            TopBar(
                player,
                title = if (tab == Tab.Challenges) "Challenges" else null,
                onAvatar = { onPopup(HomePopup.Profile) },
                onCoins = { onTab(Tab.Store) },
                onSettings = { onPopup(HomePopup.Settings) },
            )
            Crossfade(tab, Modifier.weight(1f), label = "tab") { current ->
                when (current) {
                    Tab.Challenges -> TasksTab(R.drawable.banner_challenges, Catalog.challenges, challenge = true, onTask = onTask)
                    Tab.Missions -> TasksTab(R.drawable.banner_missions, Catalog.missions, challenge = false, onTask = onTask)
                    Tab.Games -> GamesTab(onPlay, onTab)
                    Tab.Rewards -> RewardsTab(player, onTab, onWatchAd = watchAd, onInvite = invite)
                    Tab.Store -> StoreTab { context.toast("Purchases come with Google Play Billing in a later step") }
                }
            }
            BottomNav(tab, onTab)
        }
        PopupHost(popup, onDismiss = { onPopup(null) }) { current ->
            when (current) {
                HomePopup.Welcome -> WelcomeBonusPopup(
                    onClaim = {
                        player.claimWelcome()
                        context.toast("+25,000 coins!")
                        onPopup(if (player.claimedDailyToday) null else HomePopup.Daily)
                    },
                    onClose = { onPopup(if (player.claimedDailyToday) null else HomePopup.Daily) },
                )
                HomePopup.Daily -> DailyRewardsPopup(player) { onPopup(null) }
                HomePopup.Settings -> SettingsPopup(player) { onPopup(null) }
                HomePopup.Profile -> ProfilePopup(
                    player,
                    onClose = { onPopup(null) },
                    onAccount = { onPopup(null); onAccount() },
                    onSettings = { onPopup(HomePopup.Settings) },
                    onRename = { onPopup(HomePopup.Rename) },
                )
                HomePopup.Rename -> RenamePopup(player) { onPopup(HomePopup.Profile) }
            }
        }
    }
}
