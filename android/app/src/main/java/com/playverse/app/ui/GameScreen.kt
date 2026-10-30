package com.playverse.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.R
import com.playverse.app.data.Game
import com.playverse.app.data.Player
import com.playverse.app.ui.components.ButtonStyle
import com.playverse.app.ui.components.ChunkyButton
import com.playverse.app.ui.components.Crown
import com.playverse.app.ui.components.GameArt
import com.playverse.app.ui.components.OutlinedText
import com.playverse.app.ui.components.Pill
import com.playverse.app.ui.components.PopupHost
import com.playverse.app.ui.components.fmt
import com.playverse.app.ui.components.roomGlow
import com.playverse.app.ui.components.toast
import com.playverse.app.ui.popups.GameOverPopup
import com.playverse.app.ui.popups.LevelUpPopup
import com.playverse.app.ui.popups.PausedPopup
import com.playverse.app.ui.popups.ScoreCard
import com.playverse.app.ui.popups.WatchVideoPopup
import com.playverse.app.ui.theme.PV

enum class GamePopup { Paused, ExtraLife, ExtraTime, LevelUp, GameOver }

/**
 * Stands in for the Unity game until it is embedded.
 * The buttons show the in-game popups so each one can be checked on a phone.
 */
@Composable
fun GameScreen(player: Player, game: Game, onExit: () -> Unit, initialPopup: GamePopup? = null) {
    val context = LocalContext.current
    var popup by remember { mutableStateOf(initialPopup) }
    var lives by remember { mutableIntStateOf(3) }
    var seconds by remember { mutableIntStateOf(45) }
    val score = 12_850
    val card = ScoreCard(base = 8_400, bonus = 2_600, multiplier = 3, multiplied = 9_000, timeBonus = 1_500, coins = 500)

    Box(Modifier.fillMaxSize().background(Color(0xFF241238))) {
        Box(Modifier.fillMaxSize().roomGlow().background(Color(0xB0200F3A)))
        Column(
            Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()).padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                HudPill(Modifier.weight(0.8f)) {
                    Icon(Icons.Rounded.Favorite, null, tint = Color(0xFFFF3B5C), modifier = Modifier.size(26.dp))
                    Text("$lives", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                HudPill(Modifier.weight(1.2f)) {
                    Crown(Modifier.size(28.dp, 22.dp))
                    Text(fmt(score), color = PV.Gold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                HudPill(Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Timer, null, tint = Color(0xFFFFB23D), modifier = Modifier.size(24.dp))
                    Text("00:%02d".format(seconds), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    Modifier
                        .size(46.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Brush.verticalGradient(listOf(PV.GoldLight, PV.GoldDeep)))
                        .clickable { popup = GamePopup.Paused },
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Rounded.Pause, "Pause", tint = PV.Navy, modifier = Modifier.size(30.dp)) }
            }

            GameArt(
                game,
                Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
                    .shadow(10.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .border(3.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(24.dp)),
            )
            OutlinedText(game.name, 36.sp)
            Text(
                "The Unity game will load here.\nUntil then, try the in-game popups:",
                color = Color(0xFFD9CFFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ChunkyButton("Pause", ButtonStyle.Purple, { popup = GamePopup.Paused }, Modifier.weight(1f), height = 52.dp, fontSize = 19.sp)
                ChunkyButton("Level up", ButtonStyle.Purple, { popup = GamePopup.LevelUp }, Modifier.weight(1f), height = 52.dp, fontSize = 19.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ChunkyButton("Out of lives", ButtonStyle.Purple, { popup = GamePopup.ExtraLife }, Modifier.weight(1f), height = 52.dp, fontSize = 19.sp)
                ChunkyButton("Out of time", ButtonStyle.Purple, { popup = GamePopup.ExtraTime }, Modifier.weight(1f), height = 52.dp, fontSize = 19.sp)
            }
            ChunkyButton("Game over", ButtonStyle.Orange, { popup = GamePopup.GameOver }, Modifier.fillMaxWidth(), height = 52.dp, fontSize = 20.sp)
            ChunkyButton("Back to games", ButtonStyle.Red, onExit, Modifier.fillMaxWidth(0.8f), height = 52.dp, fontSize = 20.sp)
        }

        PopupHost(popup, onDismiss = { popup = null }) { current ->
            when (current) {
                GamePopup.Paused -> PausedPopup(
                    player,
                    score,
                    onResume = { popup = null },
                    onEnd = { popup = GamePopup.GameOver },
                    onHowToPlay = { context.toast("Each game will show its own rules here") },
                )
                GamePopup.ExtraLife -> WatchVideoPopup(
                    R.drawable.hero_heart,
                    "Watch a video to get",
                    "an extra Life!",
                    onWatch = { context.toast("Rewarded ads come in a later step"); lives += 1; popup = null },
                    onNoThanks = { popup = GamePopup.GameOver },
                )
                GamePopup.ExtraTime -> WatchVideoPopup(
                    R.drawable.hero_stopwatch,
                    "Watch a video to get",
                    "extra 30 seconds!",
                    onWatch = { context.toast("Rewarded ads come in a later step"); seconds = minOf(seconds + 30, 99); popup = null },
                    onNoThanks = { popup = GamePopup.GameOver },
                )
                GamePopup.LevelUp -> LevelUpPopup(player.level) {
                    player.levelUp()
                    popup = null
                }
                GamePopup.GameOver -> GameOverPopup(
                    card,
                    onDoubleCoins = { context.toast("Rewarded ads come in a later step") },
                    onNext = {
                        player.finishGame(card.total, card.coins)
                        onExit()
                    },
                )
            }
        }
    }
}

@Composable
private fun HudPill(modifier: Modifier, content: @Composable () -> Unit) {
    Row(
        modifier
            .clip(Pill)
            .background(Color(0xCC120A26))
            .border(1.5.dp, Color(0x557E57FF), Pill)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) { content() }
}
