package com.playverse.app.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.playverse.app.data.Avatar
import com.playverse.app.data.Frame
import com.playverse.app.ui.theme.PV

/** The player's round portrait, with their chosen frame around it. */
@Composable
fun AvatarImage(
    avatar: Avatar,
    frame: Frame,
    size: Dp,
    modifier: Modifier = Modifier,
    ring: Brush = Brush.verticalGradient(listOf(Color(0xFFFFD27A), Color(0xFFE0802A))),
) {
    val frameArt = frame.art
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Image(
            painterResource(avatar.art),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(if (frameArt != null) 0.7f else 1f)
                .clip(CircleShape)
                .then(if (frameArt == null) Modifier.border(size * 0.05f, ring, CircleShape) else Modifier),
        )
        if (frameArt != null) Image(painterResource(frameArt), contentDescription = null, modifier = Modifier.fillMaxSize())
    }
}

/** Small round edit button on the corner of a portrait or next to a name. */
@Composable
fun EditBadge(onClick: () -> Unit, modifier: Modifier = Modifier, size: Dp = 36.dp, color: Color = Color(0xFF3F7BFF)) {
    Box(
        modifier
            .size(size)
            .shadow(3.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .padding(2.dp)
            .clip(CircleShape)
            .background(Brush.verticalGradient(listOf(color.copy(alpha = 0.85f), color)))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

/** Gold tick shown on the selected avatar or frame. */
@Composable
fun SelectedTick(modifier: Modifier = Modifier, size: Dp = 28.dp) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.verticalGradient(listOf(PV.GoldLight, PV.GoldDeep)))
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.Rounded.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(size * 0.7f))
    }
}

/**
 * Shows [popup] centered over a dark scrim, scrolling if it is taller than the screen.
 * Back closes it.
 */
@Composable
fun <T : Any> PopupHost(popup: T?, onDismiss: () -> Unit, content: @Composable (T) -> Unit) {
    BackHandler(enabled = popup != null, onBack = onDismiss)
    AnimatedContent(
        targetState = popup,
        transitionSpec = { (fadeIn() + scaleIn(initialScale = 0.92f)) togetherWith fadeOut() },
        label = "popup",
    ) { current ->
        if (current == null) return@AnimatedContent
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .background(PV.ScrimTop)
                .clickable(remember { MutableInteractionSource() }, indication = null) {},
        ) {
            val minHeight = maxHeight
            Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Box(
                    Modifier.fillMaxWidth().heightIn(min = minHeight).systemBarsPadding().padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    content(current)
                }
            }
        }
    }
}

val Rounded14 = RoundedCornerShape(14.dp)
