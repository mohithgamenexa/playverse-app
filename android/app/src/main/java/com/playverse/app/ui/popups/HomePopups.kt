package com.playverse.app.ui.popups

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.HeadsetMic
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.R
import com.playverse.app.data.Catalog
import com.playverse.app.data.Player
import com.playverse.app.ui.components.Art
import com.playverse.app.ui.components.AvatarImage
import com.playverse.app.ui.components.ButtonStyle
import com.playverse.app.ui.components.ChunkyButton
import com.playverse.app.ui.components.CloseButton
import com.playverse.app.ui.components.CoinIcon
import com.playverse.app.ui.components.Crown
import com.playverse.app.ui.components.EditBadge
import com.playverse.app.ui.components.GoldFill
import com.playverse.app.ui.components.InnerBox
import com.playverse.app.ui.components.LevelBar
import com.playverse.app.ui.components.OutlinedText
import com.playverse.app.ui.components.Panel
import com.playverse.app.ui.components.PanelStyle
import com.playverse.app.ui.components.PvToggle
import com.playverse.app.ui.components.RibbonTitle
import com.playverse.app.ui.components.TitledPopup
import com.playverse.app.ui.components.WhiteFill
import com.playverse.app.ui.components.fmt
import com.playverse.app.ui.components.fmtShort
import com.playverse.app.ui.components.toast
import com.playverse.app.ui.theme.PV

@Composable
fun WelcomeBonusPopup(onClaim: () -> Unit, onClose: () -> Unit) {
    TitledPopup(
        PanelStyle.Purple,
        titleHeight = 190.dp,
        overlap = 60.dp,
        onClose = onClose,
        redClose = false,
        title = { RibbonTitle(listOf("Welcome" to GoldFill, "BONUS" to WhiteFill), 50.sp) },
    ) {
        Column(
            Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF241063))
                .border(2.dp, Color(0xFF8A62FF), RoundedCornerShape(22.dp))
                .padding(horizontal = 18.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("A special gift to", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("begin your journey!", color = PV.Gold, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        }
        Art(R.drawable.hero_welcome_chest, Modifier.fillMaxWidth().aspectRatio(720f / 425f))
        Row(
            Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF2A1470))
                .border(2.dp, Color(0xFF7E57FF), RoundedCornerShape(22.dp))
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CoinIcon(64.dp)
            Column(Modifier.padding(start = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedText(fmt(Catalog.WELCOME_BONUS), 44.sp, fill = GoldFill)
                Text("COINS", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
        ChunkyButton("Claim Now", ButtonStyle.Green, onClaim, Modifier.fillMaxWidth(0.9f), height = 66.dp, fontSize = 30.sp)
    }
}

@Composable
fun DailyRewardsPopup(player: Player, onClose: () -> Unit) {
    val context = LocalContext.current
    TitledPopup(
        PanelStyle.Purple,
        titleHeight = 200.dp,
        overlap = 58.dp,
        onClose = onClose,
        title = {
            Box(Modifier.fillMaxSize()) {
                Art(R.drawable.reward_gift, Modifier.align(Alignment.CenterStart).padding(top = 40.dp).size(78.dp).offset(x = 6.dp))
                Art(R.drawable.reward_gift, Modifier.align(Alignment.CenterEnd).padding(top = 40.dp).size(78.dp).offset(x = (-6).dp))
                RibbonTitle(listOf("Daily" to GoldFill, "Rewards" to WhiteFill), 48.sp)
            }
        },
    ) {
        Text("Log in daily to get awesome rewards!", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            for (i in 0..3) DayCard(player, i, Modifier.weight(1f))
        }
        Row(Modifier.padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (i in 4..6) DayCard(player, i, Modifier.weight(1f))
        }
        val claimed = player.claimedDailyToday
        ChunkyButton(
            if (claimed) "Come back tomorrow" else "Claim",
            ButtonStyle.Green,
            onClick = {
                val got = player.claimDaily()
                context.toast("+${fmt(got)} coins!")
                onClose()
            },
            modifier = Modifier.fillMaxWidth(0.85f).padding(top = 4.dp),
            height = 64.dp,
            fontSize = if (claimed) 22.sp else 30.sp,
            enabled = !claimed,
        )
    }
}

@Composable
private fun DayCard(player: Player, day: Int, modifier: Modifier) {
    val reward = Catalog.daily[day]
    val claimed = day < player.dailyClaimed
    val today = day == player.dailyToday && !player.claimedDailyToday
    val gold = today || day == 6
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier
            .then(if (today) Modifier.shadow(12.dp, shape, ambientColor = PV.Gold, spotColor = PV.Gold) else Modifier)
            .clip(shape)
            .background(Color(0xFF4B2E9E))
            .border(if (today) 3.dp else 2.dp, if (today) Color(0xFFFFD84A) else Color(0xFFA98BFF), shape),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Day ${day + 1}",
            color = if (gold) PV.Navy else Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (gold) Brush.verticalGradient(listOf(Color(0xFFFFE45C), Color(0xFFFFB21F)))
                    else Brush.verticalGradient(listOf(Color(0xFF8A62FF), Color(0xFF6A3BEA))),
                )
                .padding(vertical = 4.dp),
        )
        Box(Modifier.fillMaxWidth().aspectRatio(1.2f)) {
            Art(reward.art, Modifier.fillMaxSize(), crop = true)
            if (claimed) {
                Box(Modifier.fillMaxSize().background(Color(0x88140A3A)), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier.size(34.dp).clip(RoundedCornerShape(50)).background(Color(0xFF22B03A)).border(2.dp, Color.White, RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center,
                    ) { Icon(Icons.Rounded.Check, "Claimed", tint = Color.White, modifier = Modifier.size(24.dp)) }
                }
            }
        }
        Text(
            fmt(reward.coins),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .background(if (gold) Color(0xFF4A2414) else Color(0xFF3A2386))
                .padding(vertical = 4.dp),
        )
    }
}

@Composable
fun SettingsPopup(player: Player, onClose: () -> Unit) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(28.dp)
    Box(Modifier.fillMaxWidth().padding(horizontal = 22.dp)) {
        Column(
            Modifier
                .fillMaxWidth()
                .shadow(16.dp, shape)
                .clip(shape)
                .background(PV.Cream)
                .border(3.dp, PV.PurpleBright, shape),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color(0xFF7E4DFF), Color(0xFF5426C9))))
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Rounded.Settings, null, tint = Color.White, modifier = Modifier.size(38.dp))
                OutlinedText("Settings", 30.sp, Modifier.padding(start = 12.dp), outlineWidth = 2.dp)
            }
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingRow(Icons.Rounded.MusicNote, Color(0xFFFF5A6E), Color(0xFFE0264A), "Music") {
                    PvToggle(player.music, { player.music = it })
                }
                SettingRow(Icons.AutoMirrored.Rounded.VolumeUp, Color(0xFF4A86FF), Color(0xFF1D56D6), "Sound Effects") {
                    PvToggle(player.sound, { player.sound = it })
                }
                SettingRow(Icons.Rounded.Vibration, Color(0xFFA77BFF), Color(0xFF6A3BEA), "Haptics") {
                    PvToggle(player.haptics, { player.haptics = it })
                }
                SettingRow(Icons.Rounded.Translate, Color(0xFFFFC23D), Color(0xFFFF8A1A), "Language") {
                    LanguagePicker(player)
                }
                HorizontalDivider(Modifier.padding(vertical = 2.dp), color = PV.CardBorder)
                SettingRow(Icons.Rounded.SportsEsports, Color(0xFF5FE04A), Color(0xFF22A03A), "How to Play", onClick = {
                    context.toast("Each game shows its own How to Play")
                }) { Chevron() }
                SettingRow(Icons.Rounded.Description, Color(0xFFA77BFF), Color(0xFF6A3BEA), "Privacy Policy", onClick = {
                    context.toast("Add your privacy policy link")
                }) { Chevron() }
                SettingRow(Icons.Rounded.Shield, Color(0xFF4A86FF), Color(0xFF1D56D6), "Terms of Service", onClick = {
                    context.toast("Add your terms of service link")
                }) { Chevron() }
                SettingRow(Icons.Rounded.HeadsetMic, Color(0xFFFF5A6E), Color(0xFFE0264A), "Support", onClick = {
                    context.toast("Add your support email")
                }) { Chevron() }
                ChunkyButton(
                    "Log Out",
                    ButtonStyle.Red,
                    { context.toast("Accounts come with the online version") },
                    Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp),
                    fontSize = 24.sp,
                    leading = { Icon(Icons.AutoMirrored.Rounded.Logout, null, tint = Color.White, modifier = Modifier.size(28.dp)) },
                )
            }
        }
        CloseButton(onClose, Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = 6.dp))
    }
}

@Composable
private fun Chevron() = Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = Color(0xFF7A80A8), modifier = Modifier.size(32.dp))

@Composable
private fun IconTile(icon: ImageVector, top: Color, bottom: Color, size: Int = 46) {
    Box(
        Modifier
            .size(size.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.verticalGradient(listOf(top, bottom))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size((size * 0.58f).dp))
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    top: Color,
    bottom: Color,
    label: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFFFFF9F0))
            .border(1.dp, PV.CardBorder, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(icon, top, bottom)
        Text(label, fontSize = 19.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f).padding(start = 14.dp))
        trailing()
    }
}

@Composable
private fun LanguagePicker(player: Player) {
    var open by remember { mutableStateOf(false) }
    Box {
        Row(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF6E7CF))
                .border(1.5.dp, Color(0xFFE6CFA8), RoundedCornerShape(12.dp))
                .clickable { open = true }
                .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(player.language, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Icon(Icons.Rounded.ExpandMore, null, tint = PV.Navy)
        }
        DropdownMenu(open, onDismissRequest = { open = false }) {
            Catalog.languages.forEach { lang ->
                DropdownMenuItem(text = { Text(lang) }, onClick = { player.language = lang; open = false })
            }
        }
    }
}

@Composable
fun ProfilePopup(
    player: Player,
    onClose: () -> Unit,
    onAccount: () -> Unit,
    onSettings: () -> Unit,
    onRename: () -> Unit,
) {
    val context = LocalContext.current
    TitledPopup(
        PanelStyle.Dark,
        titleHeight = 96.dp,
        overlap = 40.dp,
        onClose = onClose,
        title = { RibbonTitle(listOf("PROFILE" to GoldFill), 40.sp, Modifier.fillMaxWidth(0.8f), crown = false) },
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box {
                Box(
                    Modifier
                        .size(112.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFF7E4DFF), Color(0xFF4A1FB8))))
                        .border(3.dp, Brush.verticalGradient(listOf(Color(0xFFFFC266), Color(0xFFE0702A))), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center,
                ) { AvatarImage(player.avatar, player.frame, 104.dp) }
                EditBadge(onAccount, Modifier.align(Alignment.BottomEnd).offset(8.dp, 8.dp))
            }
            Column(Modifier.weight(1f).padding(start = 18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(player.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, maxLines = 1, modifier = Modifier.weight(1f, fill = false))
                    EditBadge(onRename, Modifier.padding(start = 8.dp), size = 28.dp)
                }
                Text("Level ${player.level}", color = PV.Gold, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                LevelBar(player.level, player.xp, player.xpGoal, Modifier.padding(top = 8.dp).fillMaxWidth(), height = 24.dp)
            }
        }
        InnerBox(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Stat(Modifier.weight(1f), fmt(player.coins), "Coins", PV.Gold) { CoinIcon(32.dp) }
                StatDivider()
                Stat(Modifier.weight(1f), fmt(player.gamesPlayed), "Games Played", Color.White) {
                    Icon(Icons.Rounded.EmojiEvents, null, tint = PV.Gold, modifier = Modifier.size(32.dp))
                }
                StatDivider()
                Stat(Modifier.weight(1f), fmtShort(player.bestScore), "Best Score", Color.White) { Crown(Modifier.size(32.dp, 26.dp)) }
            }
        }
        MenuRow(Icons.Rounded.Person, Color(0xFF4A86FF), Color(0xFF1D56D6), "Account", "Manage your account", onAccount)
        MenuRow(Icons.Rounded.SportsEsports, Color(0xFF5FE04A), Color(0xFF22A03A), "Game Stats", "View your progress and records") {
            context.toast("Game stats arrive with the first Unity game")
        }
        MenuRow(Icons.Rounded.EmojiEvents, Color(0xFFFFC23D), Color(0xFFFF8A1A), "Achievements", "Check your achievements") {
            context.toast("Achievements are coming soon")
        }
        MenuRow(Icons.Rounded.CardGiftcard, Color(0xFFFF5A8A), Color(0xFFE0264A), "Rewards History", "View your earned rewards") {
            context.toast("Rewards history is coming soon")
        }
        MenuRow(Icons.Rounded.Settings, Color(0xFFA77BFF), Color(0xFF6A3BEA), "Settings", "Sound, Music, Notifications", onSettings)
        ChunkyButton(
            "Log Out",
            ButtonStyle.Red,
            { context.toast("Accounts come with the online version") },
            Modifier.fillMaxWidth(0.8f).padding(top = 4.dp),
            fontSize = 24.sp,
            leading = { Icon(Icons.AutoMirrored.Rounded.Logout, null, tint = Color.White, modifier = Modifier.size(28.dp)) },
        )
    }
}

@Composable
fun Stat(modifier: Modifier, value: String, label: String, valueColor: Color, icon: @Composable () -> Unit) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Text(value, color = valueColor, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1, modifier = Modifier.padding(start = 6.dp))
        }
        Text(label, color = Color(0xFFCFC6EE), fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}

@Composable
fun StatDivider() = Box(Modifier.width(1.dp).height(44.dp).background(Color(0x557E57FF)))

@Composable
private fun MenuRow(icon: ImageVector, top: Color, bottom: Color, title: String, subtitle: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFF261A52))
            .border(1.5.dp, Color(0x557E57FF), shape)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(icon, top, bottom, size = 52)
        Column(Modifier.weight(1f).padding(start = 14.dp)) {
            Text(title, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color(0xFFCFC6EE), fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1)
        }
        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = Color(0xFFCFC6EE), modifier = Modifier.size(30.dp))
    }
}

@Composable
fun RenamePopup(player: Player, onClose: () -> Unit) {
    var name by remember { mutableStateOf(player.name) }
    val valid = name.trim().length in 3..16
    Box(Modifier.fillMaxWidth().padding(horizontal = 22.dp)) {
        Panel(PanelStyle.Dark, Modifier.fillMaxWidth()) {
            OutlinedText("Your Name", 30.sp, fill = GoldFill)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it.take(16) },
                singleLine = true,
                supportingText = { Text("3 to 16 letters", color = Color(0xFFCFC6EE)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = PV.Gold,
                    unfocusedBorderColor = PV.PurpleBright,
                    cursorColor = PV.Gold,
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                modifier = Modifier.fillMaxWidth(),
            )
            ChunkyButton("Save", ButtonStyle.Green, { player.name = name.trim(); onClose() }, Modifier.fillMaxWidth(0.7f), enabled = valid)
        }
        CloseButton(onClose, Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-14).dp))
    }
}
