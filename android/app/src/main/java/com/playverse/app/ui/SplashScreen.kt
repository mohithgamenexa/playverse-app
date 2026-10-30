package com.playverse.app.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.ui.components.Pill
import com.playverse.app.ui.components.PlayverseLogo
import com.playverse.app.ui.components.PvProgress

@Composable
fun SplashScreen(onDone: () -> Unit, durationMillis: Int = 2200) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(durationMillis, easing = FastOutSlowInEasing))
        onDone()
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(Color(0xFF7B3FEA), Color(0xFF4A1FB8), Color(0xFF26107A)), radius = 1600f)),
    ) {
        Icon(
            Icons.Outlined.SportsEsports,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.09f),
            modifier = Modifier.size(260.dp).offset((-70).dp, 70.dp).rotate(-18f),
        )
        Icon(
            Icons.Rounded.StarBorder,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.09f),
            modifier = Modifier.align(Alignment.TopEnd).size(150.dp).offset(40.dp, 170.dp).rotate(14f),
        )
        Waves(Modifier.align(Alignment.BottomCenter).fillMaxWidth().fillMaxHeight(0.42f))
        Column(
            Modifier.align(Alignment.Center).padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            PlayverseLogo(64.sp)
            Box(Modifier.height(36.dp))
            PvProgress(
                progress.value,
                Modifier.fillMaxWidth(0.62f).border(3.dp, Color(0xFF7E57FF), Pill).padding(4.dp).clip(Pill),
                height = 22.dp,
                track = Color(0xFF26135E),
            )
            Text("Loading...", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Waves(modifier: Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val back = Path().apply {
            moveTo(0f, h * 0.05f)
            cubicTo(w * 0.3f, h * 0.0f, w * 0.45f, h * 0.45f, w, h * 0.25f)
            lineTo(w, h); lineTo(0f, h); close()
        }
        drawPath(back, Brush.verticalGradient(listOf(Color(0x664B1FA0), Color(0x88321070))))
        val front = Path().apply {
            moveTo(0f, h * 0.42f)
            cubicTo(w * 0.35f, h * 0.38f, w * 0.55f, h * 0.85f, w, h * 0.62f)
            lineTo(w, h); lineTo(0f, h); close()
        }
        drawPath(front, Brush.verticalGradient(listOf(Color(0xAA5A1F9A), Color(0xFF2A0E60))))
        val edge = Path().apply {
            moveTo(0f, h * 0.42f)
            cubicTo(w * 0.35f, h * 0.38f, w * 0.55f, h * 0.85f, w, h * 0.62f)
        }
        drawPath(edge, Brush.horizontalGradient(listOf(Color(0xCCFF8A5B), Color(0x55FF6FA8), Color(0xAAFF9A5B))), style = Stroke(5f))
        drawCircle(Brush.radialGradient(listOf(Color(0x33FF8A5B), Color.Transparent), Offset(w * 0.1f, h * 0.45f), w * 0.5f), w * 0.5f, Offset(w * 0.1f, h * 0.45f))
    }
}
