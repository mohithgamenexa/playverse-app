package com.playverse.app.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.R
import com.playverse.app.data.Catalog
import com.playverse.app.data.CoinPack
import com.playverse.app.data.Game
import com.playverse.app.data.Player
import com.playverse.app.data.Task
import com.playverse.app.ui.components.Art
import com.playverse.app.ui.components.ButtonStyle
import com.playverse.app.ui.components.ChunkyButton
import com.playverse.app.ui.components.CoinIcon
import com.playverse.app.ui.components.GameArt
import com.playverse.app.ui.components.GoldFill
import com.playverse.app.ui.components.OutlinedText
import com.playverse.app.ui.components.Pill
import com.playverse.app.ui.components.PvProgress
import com.playverse.app.ui.components.fmt
import com.playverse.app.ui.components.toast
import com.playverse.app.ui.theme.PV
import kotlinx.coroutines.delay

// ---------- Games ----------

@Composable
fun GamesTab(onPlay: (Game) -> Unit, onTab: (Tab) -> Unit) {
    LazyColumn(Modifier.fillMaxSize()) {
        item { SheetHeader { BannerPager(onTab) } }
        items(Catalog.games.chunked(2)) { row ->
            SheetRow {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { GameTile(it, Modifier.weight(1f)) { onPlay(it) } }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
        item { SheetBottomSpace() }
    }
}

@Composable
private fun BannerPager(onTab: (Tab) -> Unit) {
    val banners = listOf(
        R.drawable.banner_games to null,
        R.drawable.banner_rewards to Tab.Rewards,
        R.drawable.banner_store to Tab.Store,
    )
    val pager = rememberPagerState { banners.size }
    LaunchedEffect(pager) {
        while (true) {
            delay(4_000)
            pager.animateScrollToPage((pager.currentPage + 1) % banners.size)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(pager, pageSpacing = 12.dp) { page ->
            val (res, tab) = banners[page]
            Banner(res, Modifier.aspectRatio(700f / 230f), onClick = tab?.let { { onTab(it) } })
        }
        Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            repeat(banners.size) { i ->
                Box(
                    Modifier
                        .size(if (i == pager.currentPage) 22.dp else 12.dp, 7.dp)
                        .clip(Pill)
                        .background(if (i == pager.currentPage) Color(0xFFFF9F1C) else Color(0xFFD9CDB8)),
                )
            }
        }
    }
}

@Composable
fun GameTile(game: Game, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier
            .aspectRatio(1.48f)
            .shadow(5.dp, shape)
            .clip(shape)
            .background(game.labelColor)
            .clickable(onClick = onClick),
    ) {
        GameArt(game, Modifier.fillMaxWidth().fillMaxHeight(0.75f))
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(Brush.verticalGradient(listOf(game.labelColor.copy(alpha = 0.88f), game.labelColor)))
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedText(
                game.name,
                if (game.name.length > 12) 15.sp else 19.sp,
                Modifier.weight(1f),
                outline = Color(0xCC0B0A24),
                outlineWidth = 2.dp,
                textAlign = TextAlign.Start,
                maxLines = 1,
            )
            Box(
                Modifier.size(28.dp).border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, "Play", tint = Color.White, modifier = Modifier.size(22.dp))
            }
        }
        Box(Modifier.fillMaxSize().border(2.dp, Color.White.copy(alpha = 0.3f), shape))
    }
}

// ---------- Missions and Challenges ----------

@Composable
fun TasksTab(
    @DrawableRes banner: Int,
    tasks: List<Task>,
    challenge: Boolean,
    onTask: (Task) -> Unit,
) {
    LazyColumn(Modifier.fillMaxSize()) {
        item { SheetHeader { Banner(banner) } }
        items(tasks) { task -> SheetRow { TaskRow(task, challenge) { onTask(task) } } }
        item { SheetBottomSpace() }
    }
}

@Composable
private fun TaskRow(task: Task, challenge: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .shadow(3.dp, shape)
            .clip(shape)
            .background(PV.CardCream)
            .border(1.5.dp, PV.CardBorder, shape)
            .then(if (challenge) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(if (challenge) 96.dp else 80.dp, 74.dp).clip(RoundedCornerShape(14.dp))) {
            val game = task.gameId?.let { Catalog.game(it) }
            if (game != null) GameArt(game, Modifier.fillMaxSize())
            else if (task.icon != null) {
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF8A4DFF), Color(0xFF5A22C8)))), contentAlignment = Alignment.Center) {
                    Art(task.icon, Modifier.fillMaxSize().padding(4.dp))
                }
            }
        }
        Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
            Text(
                task.title,
                fontSize = when {
                    task.title.length > 17 -> 13.sp
                    task.title.length > 13 -> 15.sp
                    else -> 17.sp
                },
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(task.detail, fontSize = 13.sp, color = PV.TextMuted, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                PvProgress(task.progress.toFloat() / task.goal, Modifier.weight(1f), height = 12.dp)
                Text(
                    "${fmt(task.progress)}/${fmt(task.goal)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
        Column(
            Modifier.width(60.dp).clip(RoundedCornerShape(14.dp)).background(PV.RewardBox).padding(vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Art(R.drawable.icon_coin_pile, Modifier.size(34.dp))
            Text(fmt(task.reward), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        if (challenge) {
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, "Open", tint = PV.Navy, modifier = Modifier.size(34.dp))
        } else {
            ChunkyButton("Go", ButtonStyle.Green, onClick, Modifier.padding(start = 6.dp).width(62.dp), height = 48.dp, fontSize = 21.sp)
        }
    }
}

// ---------- Rewards ----------

@Composable
fun RewardsTab(player: Player, onTab: (Tab) -> Unit, onWatchAd: () -> Unit, onInvite: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(Modifier.fillMaxSize()) {
        item { SheetHeader { Banner(R.drawable.banner_rewards) } }
        item { SheetRow { DailyStrip(player) } }
        item {
            SheetRow {
                FreeChestRow(player.canOpenChest) {
                    val got = player.openChest()
                    context.toast("+${fmt(got)} coins!")
                }
            }
        }
        item { SheetRow { OfferRow(R.drawable.reward_video, "Watch Video", "Watch a short ad\nand get free coins!", 100, null, "Watch", onWatchAd) } }
        item { SheetRow { OfferRow(R.drawable.reward_friends, "Invite Friends", "Invite friends and\nearn bonus coins!", 200, null, "Invite", onInvite) } }
        item {
            SheetRow {
                OfferRow(R.drawable.reward_star, "Rate the Game", "Show your support\nand get free coins!", 100, null, "Rate") {
                    context.toast("Rating opens once the app is on Google Play")
                }
            }
        }
        item {
            SheetRow {
                OfferRow(R.drawable.reward_spin, "Lucky Spin", "Spin the wheel and\nwin amazing rewards!", null, R.drawable.reward_gift, "Spin") {
                    context.toast("Lucky Spin is coming soon")
                }
            }
        }
        item { SheetRow { SpecialPacksRow { onTab(Tab.Store) } } }
        item { SheetBottomSpace() }
    }
}

private val OfferGradient = Brush.horizontalGradient(listOf(Color(0xFF5A2BD6), Color(0xFFA43BC8), Color(0xFFE86AA6)))

@Composable
private fun DailyStrip(player: Player) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(20.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape)
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(Color(0xFF5A2BD6), Color(0xFF9B3BD0), Color(0xFFC85AC8))))
            .border(2.dp, Color(0xFFB79BFF), shape)
            .padding(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)) {
            Box(
                Modifier.size(44.dp).clip(CircleShape).background(Color(0xFF3A1A8A)).border(2.dp, Color(0xFFB79BFF), CircleShape),
                contentAlignment = Alignment.Center,
            ) { Art(R.drawable.reward_gift, Modifier.size(32.dp)) }
            Column(Modifier.padding(start = 10.dp)) {
                OutlinedText("Daily Rewards", 22.sp, fill = GoldFill, outlineWidth = 2.dp, textAlign = TextAlign.Start)
                Text("Log in every day and collect rewards!", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Catalog.daily.forEachIndexed { i, reward ->
                val claimed = i < player.dailyClaimed
                val today = i == player.dailyToday && !player.claimedDailyToday
                val chipShape = RoundedCornerShape(10.dp)
                Column(
                    Modifier
                        .weight(1f)
                        .clip(chipShape)
                        .background(if (today) Brush.verticalGradient(listOf(Color(0xFFFFE45C), Color(0xFFFFB21F))) else SolidColor(Color.White))
                        .border(if (today) 2.dp else 1.dp, if (today) Color(0xFFFF9F1C) else Color(0xFFE2D6FF), chipShape)
                        .clickable(enabled = today) {
                            val got = player.claimDaily()
                            context.toast("+${fmt(got)} coins!")
                        }
                        .padding(vertical = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Day ${i + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Art(reward.art, Modifier.padding(vertical = 3.dp).size(34.dp).clip(RoundedCornerShape(8.dp)), crop = true)
                    Text(shortCoins(reward.coins), fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Box(
                        Modifier
                            .padding(top = 3.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    claimed -> Color(0xFF22B03A)
                                    today -> Color(0xFFFF7A1A)
                                    else -> Color(0xFF9AA0B8)
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (claimed) Icon(Icons.Rounded.Check, "Claimed", tint = Color.White, modifier = Modifier.size(14.dp))
                        else if (!today) Icon(Icons.Rounded.Lock, "Locked", tint = Color.White, modifier = Modifier.size(12.dp))
                        else Text("!", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun shortCoins(n: Int) = if (n >= 1000) "${n / 1000}K" else "$n"

@Composable
private fun FreeChestRow(canOpen: Boolean, onOpen: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .height(112.dp)
            .shadow(4.dp, shape)
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(Color(0xFF3550D8), Color(0xFF6A2BD6), Color(0xFF9B3BD0))))
            .border(2.dp, Color(0xFFB79BFF), shape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Art(R.drawable.reward_free_chest, Modifier.width(130.dp).fillMaxHeight())
        Column(Modifier.weight(1f)) {
            OutlinedText("Free Chest", 22.sp, outlineWidth = 2.dp, textAlign = TextAlign.Start)
            Text(
                if (canOpen) "Open now and get\nexciting rewards!" else "Come back tomorrow\nfor another chest!",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
            )
        }
        ChunkyButton(
            if (canOpen) "Open" else "Opened",
            ButtonStyle.Green,
            onOpen,
            Modifier.padding(end = 10.dp).width(96.dp),
            height = 50.dp,
            fontSize = 20.sp,
            enabled = canOpen,
        )
    }
}

@Composable
private fun OfferRow(
    @DrawableRes icon: Int,
    title: String,
    subtitle: String,
    coins: Int?,
    @DrawableRes prize: Int?,
    button: String,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .height(96.dp)
            .shadow(4.dp, shape)
            .clip(shape)
            .background(OfferGradient)
            .border(2.dp, Color(0xFFFFD28A), shape)
            .padding(end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Art(icon, Modifier.width(76.dp).fillMaxHeight().padding(vertical = 6.dp))
        Column(Modifier.weight(1f)) {
            OutlinedText(title, 18.sp, outlineWidth = 2.dp, textAlign = TextAlign.Start, maxLines = 1)
            Text(subtitle, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium, lineHeight = 14.sp)
        }
        Row(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFF1DA))
                .padding(start = 6.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (coins != null) {
                Art(R.drawable.icon_coin_pile, Modifier.size(28.dp))
                Text(fmt(coins), fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
            } else if (prize != null) {
                Art(prize, Modifier.size(40.dp).padding(end = 4.dp))
            }
            ChunkyButton(button, ButtonStyle.Green, onClick, Modifier.width(82.dp), height = 46.dp, fontSize = 16.sp)
        }
    }
}

@Composable
private fun SpecialPacksRow(onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .height(104.dp)
            .shadow(4.dp, shape)
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(Color(0xFFE86AA6), Color(0xFFFFB86A), Color(0xFFFFD99A))))
            .border(2.dp, Color(0xFFFFD28A), shape)
            .padding(end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Art(R.drawable.reward_special, Modifier.width(120.dp).fillMaxHeight())
        Column(Modifier.weight(1f)) {
            OutlinedText("Special Packs", 19.sp, outlineWidth = 2.dp, textAlign = TextAlign.Start, maxLines = 1)
            Text("Get extra coins\nwith special packs!", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium, lineHeight = 15.sp)
        }
        ChunkyButton("View Packs", ButtonStyle.Gold, onClick, Modifier.width(118.dp), height = 50.dp, fontSize = 17.sp)
    }
}

// ---------- Store ----------

@Composable
fun StoreTab(onBuy: (CoinPack) -> Unit) {
    LazyColumn(Modifier.fillMaxSize()) {
        item { SheetHeader { Banner(R.drawable.banner_store) } }
        items(Catalog.packs.chunked(2)) { row ->
            SheetRow {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { PackCard(it, Modifier.weight(1f)) { onBuy(it) } }
                }
            }
        }
        item { SheetBottomSpace() }
    }
}

@Composable
private fun PackCard(pack: CoinPack, modifier: Modifier, onBuy: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier
            .shadow(4.dp, shape)
            .clip(shape)
            .background(PV.CardCream)
            .border(2.dp, PV.CardBorder, shape),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().aspectRatio(1.45f)) {
            Art(pack.art, Modifier.fillMaxSize(), crop = true)
            OutlinedText(
                pack.name,
                17.sp,
                Modifier.align(Alignment.TopStart).padding(start = 10.dp, top = 6.dp),
                outlineWidth = 2.dp,
                maxLines = 1,
            )
            if (pack.badge != null) {
                Text(
                    pack.badge,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 6.dp, end = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFFFF4D7A), Color(0xFFE0164E))))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
        }
        Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            CoinIcon(24.dp)
            Text(fmt(pack.coins), fontSize = 21.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp))
        }
        ChunkyButton(pack.price, ButtonStyle.Green, onBuy, Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 8.dp), height = 50.dp, fontSize = 22.sp)
    }
}
