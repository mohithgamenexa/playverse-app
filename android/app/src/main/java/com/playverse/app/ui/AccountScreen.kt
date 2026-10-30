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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.data.Catalog
import com.playverse.app.data.Player
import com.playverse.app.ui.components.Art
import com.playverse.app.ui.components.AvatarImage
import com.playverse.app.ui.components.CoinIcon
import com.playverse.app.ui.components.Crown
import com.playverse.app.ui.components.EditBadge
import com.playverse.app.ui.components.GoldFill
import com.playverse.app.ui.components.InnerBox
import com.playverse.app.ui.components.LevelBar
import com.playverse.app.ui.components.Panel
import com.playverse.app.ui.components.PanelStyle
import com.playverse.app.ui.components.RibbonTitle
import com.playverse.app.ui.components.SelectedTick
import com.playverse.app.ui.components.RoomBackdrop
import com.playverse.app.ui.components.fmt
import com.playverse.app.ui.popups.Stat
import com.playverse.app.ui.popups.StatDivider
import com.playverse.app.ui.theme.PV

/** Pick an avatar and a profile frame. */
@Composable
fun AccountScreen(player: Player, onBack: () -> Unit, onRename: () -> Unit, initialTab: Int = 0) {
    var tab by remember { mutableIntStateOf(initialTab) }
    Box(Modifier.fillMaxSize().background(Color(0xFF2A1640))) {
        RoomBackdrop(Modifier.fillMaxSize(), dim = Color(0x66200F3A))
        Column(Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 14.dp)) {
            Box(Modifier.fillMaxWidth().height(84.dp)) {
                Box(
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(50.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFF9A6BFF), Color(0xFF5A2BD6))))
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, "Back", tint = Color.White, modifier = Modifier.size(40.dp))
                }
                RibbonTitle(listOf("ACCOUNT" to GoldFill), 34.sp, Modifier.align(Alignment.Center).fillMaxWidth(0.62f), crown = false)
            }
            Panel(PanelStyle.Dark, Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        AvatarImage(player.avatar, player.frame, 116.dp)
                        EditBadge(onRename, Modifier.align(Alignment.BottomEnd), color = Color(0xFF7E4DFF))
                    }
                    Column(Modifier.weight(1f).padding(start = 16.dp)) {
                        Text(player.name, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("Level ${player.level}", color = PV.Gold, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        LevelBar(player.level, player.xp, player.xpGoal, Modifier.padding(top = 8.dp).fillMaxWidth(), height = 24.dp)
                    }
                }
                InnerBox(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Stat(Modifier.weight(1f), fmt(player.coins), "Coins", PV.Gold) { CoinIcon(36.dp) }
                        StatDivider()
                        Stat(Modifier.weight(1f), fmt(player.bestScore), "Best Score", Color.White) { Crown(Modifier.size(36.dp, 28.dp)) }
                    }
                }
            }
            Panel(PanelStyle.Dark, Modifier.fillMaxWidth().weight(1f).padding(top = 10.dp, bottom = 12.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TabButton("Avatars", tab == 0, Modifier.weight(1f)) { tab = 0 }
                    TabButton("Profile Frame", tab == 1, Modifier.weight(1f)) { tab = 1 }
                }
                LazyVerticalGrid(
                    GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    if (tab == 0) {
                        items(Catalog.avatars) { avatar ->
                            Cell(selected = avatar.id == player.avatarId, onClick = { player.avatarId = avatar.id }) {
                                Art(avatar.art, Modifier.fillMaxSize(), crop = true)
                            }
                        }
                    } else {
                        items(Catalog.frames) { frame ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Cell(selected = frame.id == player.frameId, onClick = { player.frameId = frame.id }) {
                                    val art = frame.art
                                    if (art == null) AvatarImage(player.avatar, frame, 80.dp, Modifier.padding(4.dp))
                                    else Art(art, Modifier.fillMaxSize().padding(2.dp))
                                }
                                Text(frame.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier
            .height(50.dp)
            .clip(shape)
            .background(
                if (selected) Brush.verticalGradient(listOf(Color(0xFFFFD84A), Color(0xFFFFAE1F)))
                else Brush.verticalGradient(listOf(Color(0xFF2E2266), Color(0xFF241A52))),
            )
            .border(1.5.dp, if (selected) Color(0xFFFFE89A) else Color(0x557E57FF), shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = if (selected) PV.Navy else Color(0xFFB9AEE6), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Cell(selected: Boolean, onClick: () -> Unit, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Box(Modifier.fillMaxWidth().aspectRatio(1f)) {
        Box(
            Modifier
                .fillMaxSize()
                .then(if (selected) Modifier.shadow(10.dp, shape, ambientColor = PV.Gold, spotColor = PV.Gold) else Modifier)
                .clip(shape)
                .background(Brush.verticalGradient(listOf(Color(0xFF34266E), Color(0xFF231850))))
                .border(if (selected) 3.dp else 1.5.dp, if (selected) Color(0xFFFFC23D) else Color(0x667E57FF), shape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) { content() }
        if (selected) SelectedTick(Modifier.align(Alignment.BottomEnd).offset(4.dp, 4.dp))
    }
}
