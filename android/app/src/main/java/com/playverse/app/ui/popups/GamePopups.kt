package com.playverse.app.ui.popups

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Casino
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowRight
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.R
import com.playverse.app.data.Catalog
import com.playverse.app.data.Player
import com.playverse.app.ui.components.Art
import com.playverse.app.ui.components.ButtonLabel
import com.playverse.app.ui.components.ButtonStyle
import com.playverse.app.ui.components.ChunkyButton
import com.playverse.app.ui.components.CloseButton
import com.playverse.app.ui.components.CoinIcon
import com.playverse.app.ui.components.Crown
import com.playverse.app.ui.components.GoldFill
import com.playverse.app.ui.components.InnerBox
import com.playverse.app.ui.components.OutlinedText
import com.playverse.app.ui.components.Panel
import com.playverse.app.ui.components.PanelStyle
import com.playverse.app.ui.components.PvToggle
import com.playverse.app.ui.components.RibbonTitle
import com.playverse.app.ui.components.TitledPopup
import com.playverse.app.ui.components.WhiteFill
import com.playverse.app.ui.components.fmt
import com.playverse.app.ui.theme.PV

private val RibbonRed = Color(0xFFD9283F)
private val GoldRim = Brush.horizontalGradient(listOf(Color(0xFFFFC266), Color(0xFFE0702A)))

@Composable
fun LevelUpPopup(fromLevel: Int, onCollect: () -> Unit) {
    TitledPopup(
        PanelStyle.Dark,
        titleHeight = 220.dp,
        overlap = 70.dp,
        title = { RibbonTitle(listOf("LEVEL" to GoldFill, "UP!" to WhiteFill), 58.sp, ribbon = RibbonRed) },
    ) {
        OutlinedText("Congratulations!", 30.sp, fill = GoldFill, outline = Color(0xFF5A2A0A), outlineWidth = 2.dp)
        Text("You reached a new level!", color = Color(0xFFE6DDFF), fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        InnerBox(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                LevelShield(fromLevel, gold = false)
                Icon(Icons.Rounded.KeyboardDoubleArrowRight, null, tint = PV.GoldDeep, modifier = Modifier.size(48.dp))
                LevelShield(fromLevel + 1, gold = true)
            }
        }
        Box(Modifier.fillMaxWidth()) {
            InnerBox(Modifier.fillMaxWidth().padding(top = 14.dp), border = GoldRim) {
                Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Art(R.drawable.hero_coin_stack, Modifier.weight(1f).height(76.dp))
                    RewardAmount(Catalog.LEVEL_UP_REWARD)
                }
            }
            Text(
                "Level Up Reward",
                color = PV.Gold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF2A1A12))
                    .border(1.5.dp, GoldRim, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 3.dp),
            )
        }
        ChunkyButton("Collect", ButtonStyle.Green, onCollect, Modifier.fillMaxWidth(0.85f), height = 66.dp, fontSize = 32.sp)
    }
}

@Composable
private fun RewardAmount(coins: Int) {
    Row(
        Modifier.clip(RoundedCornerShape(18.dp)).background(Color(0xFF1A1036)).padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoinIcon(42.dp)
        OutlinedText(fmt(coins), 40.sp, Modifier.padding(start = 8.dp), fill = GoldFill, outline = Color(0xFF5A2A0A), outlineWidth = 2.dp)
    }
}

/** Shield badge with "Level N" on it, from the Level Up popup. */
@Composable
private fun LevelShield(level: Int, gold: Boolean) {
    Box(Modifier.size(92.dp, 104.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val shield = Path().apply {
                moveTo(w * 0.12f, h * 0.2f)
                quadraticTo(w * 0.5f, h * 0.08f, w * 0.88f, h * 0.2f)
                lineTo(w * 0.88f, h * 0.58f)
                quadraticTo(w * 0.86f, h * 0.82f, w * 0.5f, h * 0.98f)
                quadraticTo(w * 0.14f, h * 0.82f, w * 0.12f, h * 0.58f)
                close()
            }
            val fill = if (gold) listOf(Color(0xFFB04CE8), Color(0xFF6A1FB8)) else listOf(Color(0xFF5A6BFF), Color(0xFF3A2AB8))
            val rim = if (gold) listOf(Color(0xFFFFE45C), Color(0xFFFF9F1C)) else listOf(Color(0xFFB9C4FF), Color(0xFF6A7AE0))
            drawPath(shield, Brush.verticalGradient(rim), style = Stroke(w * 0.1f, join = StrokeJoin.Round))
            drawPath(shield, Brush.verticalGradient(fill))
        }
        Crown(Modifier.align(Alignment.TopCenter).size(36.dp, 26.dp).offset(y = (-8).dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 10.dp)) {
            Text("Level", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            OutlinedText("$level", 34.sp, fill = if (gold) GoldFill else WhiteFill, outlineWidth = 2.dp, lineHeight = 34.sp)
        }
    }
}

@Composable
fun PausedPopup(player: Player, score: Int, onResume: () -> Unit, onEnd: () -> Unit, onHowToPlay: () -> Unit) {
    TitledPopup(
        PanelStyle.Purple,
        titleHeight = 170.dp,
        overlap = 50.dp,
        onClose = onResume,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(82.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Brush.verticalGradient(listOf(Color(0xFF9A6BFF), Color(0xFF5A2BD6))))
                        .border(4.dp, Color(0xFFFFC23D), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        repeat(2) {
                            Box(Modifier.size(13.dp, 36.dp).clip(RoundedCornerShape(5.dp)).background(Brush.verticalGradient(listOf(PV.GoldLight, PV.GoldDeep))))
                        }
                    }
                }
                OutlinedText("PAUSED", 54.sp, Modifier.offset(y = (-10).dp), fill = GoldFill, outlineWidth = 4.dp)
            }
        },
    ) {
        InnerBox(Modifier.fillMaxWidth()) {
            Text("CURRENT SCORE", color = Color(0xFFCFC6EE), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Crown(Modifier.size(48.dp, 38.dp))
                OutlinedText(fmt(score), 44.sp, Modifier.padding(start = 12.dp), fill = GoldFill, outlineWidth = 2.dp)
            }
        }
        InnerBox(Modifier.fillMaxWidth()) {
            ToggleRow(Icons.Rounded.MusicNote, Color(0xFFB77BFF), Color(0xFF7A3BE0), "Music", player.music) { player.music = it }
            ToggleRow(Icons.AutoMirrored.Rounded.VolumeUp, Color(0xFF4AA6FF), Color(0xFF1D56D6), "Sound", player.sound) { player.sound = it }
            ToggleRow(Icons.Rounded.Vibration, Color(0xFFFFB23D), Color(0xFFFF7A1A), "Haptics", player.haptics) { player.haptics = it }
        }
        ChunkyButton(onHowToPlay, ButtonStyle.Blue, Modifier.fillMaxWidth()) {
            Icon(Icons.AutoMirrored.Rounded.MenuBook, null, tint = Color.White, modifier = Modifier.size(32.dp))
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { ButtonLabel("How to Play", ButtonStyle.Blue, 24.sp) }
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = Color.White, modifier = Modifier.size(30.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ChunkyButton(
                "End Now", ButtonStyle.Red, onEnd, Modifier.weight(1f), fontSize = 22.sp,
                leading = { Icon(Icons.AutoMirrored.Rounded.Logout, null, tint = Color.White, modifier = Modifier.size(26.dp)) },
            )
            ChunkyButton(
                "Resume", ButtonStyle.Green, onResume, Modifier.weight(1f), fontSize = 22.sp,
                leading = { Icon(Icons.Rounded.PlayArrow, null, tint = Color.White, modifier = Modifier.size(30.dp)) },
            )
        }
    }
}

@Composable
private fun ToggleRow(icon: ImageVector, top: Color, bottom: Color, label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(48.dp).clip(CircleShape).background(Brush.verticalGradient(listOf(top, bottom))).border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center,
        ) { Icon(icon, null, tint = Color.White, modifier = Modifier.size(28.dp)) }
        Text(label, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f).padding(start = 14.dp))
        PvToggle(checked, onChange)
    }
}

/** "Watch a video to get an extra Life!" and "+30 seconds" offers. */
@Composable
fun WatchVideoPopup(
    @DrawableRes hero: Int,
    line1: String,
    line2: String,
    onWatch: () -> Unit,
    onNoThanks: () -> Unit,
) {
    Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        Panel(PanelStyle.Purple, Modifier.fillMaxWidth().padding(top = 18.dp)) {
            Art(hero, Modifier.fillMaxWidth(0.9f).aspectRatio(1.3f))
            InnerBox(Modifier.fillMaxWidth()) {
                Text(line1, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                OutlinedText(line2, 36.sp, fill = GoldFill, outlineWidth = 3.dp, maxLines = 1)
            }
            ChunkyButton(
                "Watch Video", ButtonStyle.Green, onWatch, Modifier.fillMaxWidth(0.9f), height = 66.dp, fontSize = 28.sp,
                leading = { Icon(Icons.Rounded.OndemandVideo, null, tint = Color.White, modifier = Modifier.size(36.dp)) },
            )
            Text(
                "No Thanks",
                color = Color(0xFFB9A6FF),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable(onClick = onNoThanks).padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }
        CloseButton(onNoThanks, Modifier.align(Alignment.TopEnd).offset(x = 8.dp))
    }
}

/** The end-of-game breakdown each Unity game will send back. */
data class ScoreCard(val base: Int, val bonus: Int, val multiplier: Int, val multiplied: Int, val timeBonus: Int, val coins: Int) {
    val total get() = base + bonus + multiplied + timeBonus
}

@Composable
fun GameOverPopup(card: ScoreCard, onDoubleCoins: () -> Unit, onNext: () -> Unit) {
    TitledPopup(
        PanelStyle.Dark,
        titleHeight = 220.dp,
        overlap = 66.dp,
        title = { RibbonTitle(listOf("GAME" to GoldFill, "OVER" to WhiteFill), 58.sp, ribbon = RibbonRed) },
    ) {
        InnerBox(Modifier.fillMaxWidth()) {
            Text("SCORE CARD", color = Color(0xFFCFC6EE), fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
            ScoreLine({ Icon(Icons.Rounded.Star, null, tint = PV.Gold, modifier = Modifier.size(28.dp)) }, "Base Score", card.base)
            ScoreLine({ Icon(Icons.Rounded.Casino, null, tint = Color.White, modifier = Modifier.size(26.dp)) }, "Bonus Score", card.bonus)
            ScoreLine({ CoinIcon(26.dp) }, "Multiplier (x${card.multiplier})", card.multiplied)
            ScoreLine({ Icon(Icons.Rounded.Timer, null, tint = Color(0xFFFFB23D), modifier = Modifier.size(28.dp)) }, "Time Bonus", card.timeBonus)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF1A1036))
                .border(2.dp, GoldRim, RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedText("TOTAL SCORE", 22.sp, Modifier.weight(1f), fill = GoldFill, outline = Color(0xFF5A2A0A), outlineWidth = 1.5.dp, textAlign = TextAlign.Start)
            OutlinedText(fmt(card.total), 36.sp, fill = GoldFill, outline = Color(0xFF5A2A0A), outlineWidth = 2.dp)
        }
        InnerBox(Modifier.fillMaxWidth()) {
            Text("COINS EARNED", color = Color(0xFFCFC6EE), fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Art(R.drawable.hero_coin_stack, Modifier.weight(1f).height(66.dp))
                RewardAmount(card.coins)
            }
        }
        ChunkyButton(onDoubleCoins, ButtonStyle.Orange, Modifier.fillMaxWidth(), height = 70.dp) {
            Icon(Icons.Rounded.OndemandVideo, null, tint = Color.White, modifier = Modifier.size(40.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Watch Video", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, lineHeight = 18.sp)
                OutlinedText("Get 2x Coins", 25.sp, fill = GoldFill, outline = Color(0xFF8A3A00), outlineWidth = 1.5.dp, shadow = false, lineHeight = 26.sp)
            }
        }
        ChunkyButton("Next", ButtonStyle.Green, onNext, Modifier.fillMaxWidth(0.8f), height = 64.dp, fontSize = 32.sp)
    }
}

@Composable
private fun ScoreLine(icon: @Composable () -> Unit, label: String, value: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x33FFFFFF))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.width(32.dp), contentAlignment = Alignment.Center) { icon() }
        Text(label, color = Color(0xFFE6DDFF), fontSize = 17.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f).padding(start = 8.dp))
        Text(fmt(value), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
