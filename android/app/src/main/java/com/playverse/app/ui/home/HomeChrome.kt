package com.playverse.app.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.R
import com.playverse.app.data.Player
import com.playverse.app.ui.components.AvatarImage
import com.playverse.app.ui.components.CoinIcon
import com.playverse.app.ui.components.GoldFill
import com.playverse.app.ui.components.OutlinedText
import com.playverse.app.ui.components.Pill
import com.playverse.app.ui.components.PvProgress
import com.playverse.app.ui.components.PlayverseLogo
import com.playverse.app.ui.components.StarBadge
import com.playverse.app.ui.components.fmtShort
import com.playverse.app.ui.theme.PV

enum class Tab(val label: String, @DrawableRes val icon: Int) {
    Challenges("Challenges", R.drawable.nav_challenges),
    Missions("Missions", R.drawable.nav_missions),
    Games("Games", R.drawable.nav_games),
    Rewards("Rewards", R.drawable.nav_rewards),
    Store("Store", R.drawable.nav_store),
}

/** Avatar and level on the left, logo (or page title) in the middle, coins and settings on the right. */
@Composable
fun TopBar(
    player: Player,
    title: String?,
    onAvatar: () -> Unit,
    onCoins: () -> Unit,
    onSettings: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp)
            .height(76.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            Modifier.clickable(onClick = onAvatar),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarImage(
                player.avatar,
                player.frame,
                50.dp,
                ring = Brush.verticalGradient(listOf(Color.White, Color(0xFF3F9BFF))),
            )
            Box(Modifier.offset(x = (-6).dp), contentAlignment = Alignment.CenterStart) {
                PvProgress(
                    player.xp.toFloat() / player.xpGoal,
                    Modifier.padding(start = 16.dp).width(40.dp).border(2.dp, Color(0xFF0E1630), Pill),
                    height = 14.dp,
                    track = Color(0xFF1A2147),
                )
                StarBadge(player.level, 28.dp)
            }
        }
        // The logo shrinks to fit narrow phones instead of running under the coins.
        BoxWithConstraints(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            if (title == null) {
                PlayverseLogo((maxWidth.value / 5f).coerceAtMost(26f).sp)
            } else {
                OutlinedText(title, (maxWidth.value / 5.6f).coerceAtMost(28f).sp, fill = GoldFill, outlineWidth = 2.5.dp, maxLines = 1)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                Modifier
                    .clip(Pill)
                    .background(Color(0xCC14102A))
                    .clickable(onClick = onCoins)
                    .padding(start = 2.dp, end = 3.dp, top = 2.dp, bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CoinIcon(24.dp)
                Text(
                    fmtShort(player.coins),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                Box(
                    Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Brush.verticalGradient(listOf(Color(0xFF72EC52), Color(0xFF22B03A)))),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Rounded.Add, "Get coins", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
            Box(
                Modifier
                    .size(38.dp)
                    .shadow(3.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(Color(0xFF2A3E78), Color(0xFF14204A))))
                    .border(2.dp, Color(0xFF5B78C8), CircleShape)
                    .clickable(onClick = onSettings),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Settings, "Settings", tint = Color.White, modifier = Modifier.size(26.dp))
            }
        }
    }
}

/** Navy tab bar; the selected tab is a raised yellow tile. */
@Composable
fun BottomNav(selected: Tab, onSelect: (Tab) -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .matchParentSize()
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Brush.verticalGradient(listOf(PV.NavBarTop, PV.NavBarBottom)))
                .border(2.dp, Color(0xFF3A5594), RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
        )
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 4.dp)
                .padding(bottom = 6.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Tab.entries.forEach { tab ->
                NavItem(tab, tab == selected, Modifier.weight(1f)) { onSelect(tab) }
            }
        }
    }
}

@Composable
private fun NavItem(tab: Tab, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(modifier.height(94.dp).clickable(onClick = onClick), contentAlignment = Alignment.BottomCenter) {
        if (selected) {
            Column(
                Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxWidth()
                    .height(88.dp)
                    .shadow(8.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFFFFE45C), Color(0xFFFFB21F))))
                    .border(2.dp, Color(0xFFFFF1A8), RoundedCornerShape(24.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(painterResource(tab.icon), null, Modifier.size(46.dp))
                Text(tab.label, color = PV.Navy, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
        } else {
            Column(
                Modifier.padding(bottom = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(tab.icon), null, Modifier.size(40.dp))
                Text(tab.label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
        }
    }
}

/** A wide rounded banner at the top of each tab. */
@Composable
fun Banner(@DrawableRes res: Int, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    val painter = painterResource(res)
    val shape = RoundedCornerShape(20.dp)
    Image(
        painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
            .shadow(8.dp, shape)
            .clip(shape)
            .border(3.dp, Color.White.copy(alpha = 0.85f), shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    )
}

/** The banner sits half on the warm background and half on the cream sheet below it. */
@Composable
fun SheetHeader(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(PV.Cream),
        )
        Column(Modifier.padding(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 8.dp)) { content() }
    }
}

/** Wraps list rows so the cream sheet continues behind them. */
@Composable
fun SheetRow(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth().background(PV.Cream).padding(horizontal = 12.dp, vertical = 5.dp)) { content() }
}

@Composable
fun SheetBottomSpace() = Spacer(Modifier.fillMaxWidth().height(16.dp).background(PV.Cream))
