package com.playverse.app.ui.components

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playverse.app.R
import com.playverse.app.ui.theme.Fredoka
import com.playverse.app.ui.theme.PV
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun fmt(n: Int): String = NumberFormat.getIntegerInstance(Locale.US).format(n)

/** Short form for tight spots: 12,850 → 12.8K, 1,250,000 → 1.2M. */
fun fmtShort(n: Int): String = when {
    n >= 1_000_000 -> String.format(Locale.US, "%.1fM", n / 1_000_000f).replace(".0M", "M")
    n >= 100_000 -> "${n / 1000}K"
    else -> fmt(n)
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

val Pill = RoundedCornerShape(percent = 50)

/**
 * Chunky 3D cartoon text, like the painted titles: a dark extruded base under a thick outline,
 * a gradient fill, and a glossy shine across the top. Small labels pass `shadow = false` for a flat outline.
 */
@Composable
fun OutlinedText(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    fill: Brush = SolidColor(Color.White),
    outline: Color = PV.PurpleOutline,
    outlineWidth: Dp = 3.dp,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    shadow: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    depth: Dp = if (shadow) outlineWidth * 0.9f else 0.dp,
) {
    val strokePx = with(LocalDensity.current) { (outlineWidth * 2).toPx() }
    val base = TextStyle(
        fontFamily = Fredoka,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        textAlign = textAlign,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
    )
    val stroke = base.copy(color = outline, drawStyle = Stroke(width = strokePx, join = StrokeJoin.Round))
    Box(modifier.padding(bottom = depth)) {
        if (depth > 0.dp) {
            Text(
                text,
                Modifier.offset(y = depth),
                maxLines = maxLines,
                style = stroke.copy(
                    color = outline.darken(0.55f),
                    shadow = Shadow(Color(0x66000000), Offset(0f, strokePx * 0.5f), strokePx * 1.2f),
                ),
            )
        }
        Text(text, maxLines = maxLines, style = stroke)
        Text(text, maxLines = maxLines, style = base.copy(brush = fill))
        if (shadow) {
            Text(text, maxLines = maxLines, style = base.copy(brush = Gloss))
        }
    }
}

private val Gloss = Brush.verticalGradient(
    0f to Color.White.copy(alpha = 0.55f),
    0.42f to Color.White.copy(alpha = 0.12f),
    0.43f to Color.Transparent,
)

fun Color.darken(factor: Float) = copy(red = red * factor, green = green * factor, blue = blue * factor)

val GoldFill = Brush.verticalGradient(0f to Color(0xFFFFF6A8), 0.45f to Color(0xFFFFD42E), 1f to Color(0xFFFF9410))
val WhiteFill = Brush.verticalGradient(0f to Color.White, 0.5f to Color(0xFFF3EDFF), 1f to Color(0xFFC4B0FF))

enum class ButtonStyle(val top: Color, val bottom: Color, val edge: Color, val text: Color, val outline: Color?) {
    Green(Color(0xFF72EC52), Color(0xFF22B03A), Color(0xFF138A2C), Color.White, Color(0xFF0F7A28)),
    Red(Color(0xFFFF5A78), Color(0xFFE21B4D), Color(0xFFA5113A), Color.White, Color(0xFF9C0F35)),
    Gold(Color(0xFFFFE45C), Color(0xFFFFAE1F), Color(0xFFD97A0C), PV.Navy, null),
    Blue(Color(0xFF4A86FF), Color(0xFF1D46D6), Color(0xFF15309A), Color.White, Color(0xFF13308F)),
    Orange(Color(0xFFFFC23D), Color(0xFFFF8A1A), Color(0xFFCF5F0A), Color.White, Color(0xFFB4510A)),
    Purple(Color(0xFF9A6BFF), Color(0xFF5A2BD6), Color(0xFF3B1A9A), Color.White, Color(0xFF34168A)),
    Disabled(Color(0xFFC9C3D6), Color(0xFF9F98B0), Color(0xFF7D7690), Color.White, Color(0xFF6E6880)),
}

/** The glossy pill buttons used everywhere ("Claim", "Go", "Watch", prices). */
@Composable
fun ChunkyButton(
    onClick: () -> Unit,
    style: ButtonStyle,
    modifier: Modifier = Modifier,
    height: Dp = 58.dp,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f, label = "press")
    val s = if (enabled) style else ButtonStyle.Disabled
    Box(
        modifier
            .height(height)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(5.dp, Pill)
            .clip(Pill)
            .background(s.edge.darken(0.8f))
            .clickable(source, indication = null, enabled = enabled, onClick = onClick)
            .padding(start = 1.5.dp, end = 1.5.dp, top = 1.5.dp)
            .clip(Pill)
            .background(s.edge)
            .padding(bottom = 4.5.dp)
            .clip(Pill)
            .background(Brush.verticalGradient(listOf(s.top, s.top, s.bottom)))
            .border(1.dp, Color.White.copy(alpha = 0.35f), Pill),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp)
                .fillMaxWidth(0.86f)
                .fillMaxHeight(0.4f)
                .clip(Pill)
                .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.45f), Color.White.copy(alpha = 0.08f)))),
        )
        Row(
            Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            content = content,
        )
    }
}

@Composable
fun ChunkyButton(
    text: String,
    style: ButtonStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 58.dp,
    fontSize: TextUnit = 26.sp,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
) {
    ChunkyButton(onClick, style, modifier, height, enabled) {
        leading?.invoke()
        ButtonLabel(text, if (enabled) style else ButtonStyle.Disabled, fontSize)
    }
}

@Composable
fun ButtonLabel(text: String, style: ButtonStyle, fontSize: TextUnit, lineHeight: TextUnit = TextUnit.Unspecified) {
    val outline = style.outline
    if (outline == null) {
        Text(text, color = style.text, fontSize = fontSize, fontWeight = FontWeight.Bold, maxLines = 1, lineHeight = lineHeight)
    } else {
        OutlinedText(text, fontSize, fill = SolidColor(style.text), outline = outline, outlineWidth = 1.5.dp, shadow = false, lineHeight = lineHeight, depth = 1.5.dp)
    }
}

/** Round red (or purple) close button. */
@Composable
fun CloseButton(onClick: () -> Unit, modifier: Modifier = Modifier, red: Boolean = true, size: Dp = 48.dp) {
    val colors = if (red) listOf(Color(0xFFFF5566), Color(0xFFD3123A)) else listOf(Color(0xFF9A6BFF), Color(0xFF5426C9))
    Box(
        modifier
            .size(size)
            .shadow(5.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .padding(3.dp)
            .clip(CircleShape)
            .background(Brush.verticalGradient(colors))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(size * 0.62f))
    }
}

fun starPath(center: Offset, outer: Float, inner: Float, points: Int = 5): Path = Path().apply {
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) outer else inner
        val a = -PI / 2 + i * PI / points
        val p = Offset(center.x + (r * cos(a)).toFloat(), center.y + (r * sin(a)).toFloat())
        if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
    }
    close()
}

@Composable
fun CoinIcon(size: Dp, modifier: Modifier = Modifier) {
    Canvas(modifier.size(size)) {
        val r = this.size.minDimension / 2
        drawCircle(Color(0xFFD97A00), r)
        drawCircle(Brush.verticalGradient(listOf(Color(0xFFFFE877), Color(0xFFFFB300))), r * 0.86f)
        drawCircle(Color(0xFFE89A00), r * 0.64f, style = Stroke(r * 0.08f))
        val star = starPath(center, r * 0.44f, r * 0.2f)
        drawPath(star, Color(0xFFFFF3B0))
        drawPath(star, Color(0xFFE08A00), style = Stroke(r * 0.07f, join = StrokeJoin.Round))
    }
}

/** Gold star with the player's level on it. */
@Composable
fun StarBadge(level: Int, size: Dp, modifier: Modifier = Modifier) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val r = this.size.minDimension / 2
            val star = starPath(center, r, r * 0.55f)
            drawPath(star, Color(0xFFC86A00), style = Stroke(r * 0.18f, join = StrokeJoin.Round))
            drawPath(star, Brush.verticalGradient(listOf(Color(0xFFFFE45C), Color(0xFFFFA41B))))
        }
        OutlinedText("$level", (size.value * 0.36f).sp, outline = Color(0xFFB0560A), outlineWidth = 1.5.dp, shadow = false)
    }
}

@Composable
fun Crown(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val body = Path().apply {
            moveTo(w * 0.08f, h * 0.3f)
            lineTo(w * 0.3f, h * 0.58f)
            lineTo(w * 0.5f, h * 0.12f)
            lineTo(w * 0.7f, h * 0.58f)
            lineTo(w * 0.92f, h * 0.3f)
            lineTo(w * 0.84f, h * 0.84f)
            lineTo(w * 0.16f, h * 0.84f)
            close()
        }
        val gold = Brush.verticalGradient(listOf(Color(0xFFFFEA6B), Color(0xFFFFA915)))
        drawPath(body, PV.GoldEdge, style = Stroke(w * 0.07f, join = StrokeJoin.Round))
        drawPath(body, gold)
        drawRoundRect(PV.GoldEdge, Offset(w * 0.13f, h * 0.74f), Size(w * 0.74f, h * 0.22f), CornerRadius(h * 0.08f))
        drawRoundRect(gold, Offset(w * 0.16f, h * 0.76f), Size(w * 0.68f, h * 0.17f), CornerRadius(h * 0.06f))
        listOf(Offset(w * 0.08f, h * 0.28f), Offset(w * 0.5f, h * 0.1f), Offset(w * 0.92f, h * 0.28f)).forEach {
            drawCircle(PV.GoldEdge, w * 0.075f, it)
            drawCircle(Color(0xFFFFE45C), w * 0.055f, it)
        }
        drawCircle(Color(0xFF9B4DFF), w * 0.06f, Offset(w * 0.5f, h * 0.58f))
    }
}

/** The Playverse wordmark: crown, "Play" in gold, "verse" in white, gold gamepad underneath. */
@Composable
fun PlayverseLogo(fontSize: TextUnit, modifier: Modifier = Modifier) {
    val unit = with(LocalDensity.current) { fontSize.toDp() }
    val outline = unit * 0.18f
    val spacing = (-0.03).em(fontSize)
    // Two identical rows: the first lays down one shared plaque, the second the fills on top,
    // so neither word's outline covers the other's letters.
    @Composable
    fun Word(plaque: Boolean) = Row(Modifier.padding(top = unit * 0.46f)) {
        val clear = SolidColor(Color.Transparent)
        OutlinedText("Play", fontSize, fill = if (plaque) clear else GoldFill, outline = if (plaque) TitleOutline else Color.Transparent, outlineWidth = outline, letterSpacing = spacing, depth = if (plaque) outline * 0.9f else 0.dp)
        OutlinedText("verse", fontSize, Modifier.offset(x = -unit * 0.1f), fill = if (plaque) clear else WhiteFill, outline = if (plaque) TitleOutline else Color.Transparent, outlineWidth = outline, letterSpacing = spacing, depth = if (plaque) outline * 0.9f else 0.dp)
    }
    Box(modifier, contentAlignment = Alignment.TopCenter) {
        Sparkles(Modifier.matchParentSize())
        Crown(Modifier.size(unit * 1.05f, unit * 0.78f))
        Word(plaque = true)
        Gamepad(Modifier.padding(top = unit * 1.38f).size(unit * 0.8f, unit * 0.52f))
        Word(plaque = false)
    }
}

/** A chunky gold gamepad with a dark outline, tucked under the logo. */
@Composable
fun Gamepad(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val body = Path().apply {
            moveTo(w * 0.22f, h * 0.12f)
            lineTo(w * 0.78f, h * 0.12f)
            cubicTo(w * 0.98f, h * 0.12f, w * 1.02f, h * 0.95f, w * 0.86f, h * 0.95f)
            cubicTo(w * 0.74f, h * 0.95f, w * 0.7f, h * 0.68f, w * 0.6f, h * 0.68f)
            lineTo(w * 0.4f, h * 0.68f)
            cubicTo(w * 0.3f, h * 0.68f, w * 0.26f, h * 0.95f, w * 0.14f, h * 0.95f)
            cubicTo(-w * 0.02f, h * 0.95f, w * 0.02f, h * 0.12f, w * 0.22f, h * 0.12f)
            close()
        }
        drawPath(body, TitleOutline, style = Stroke(h * 0.22f, join = StrokeJoin.Round))
        drawPath(body, Brush.verticalGradient(listOf(Color(0xFFFFE877), Color(0xFFFFB300), Color(0xFFFF8F0A))))
        val d = TitleOutline
        drawRect(d, Offset(w * 0.17f, h * 0.36f), Size(w * 0.17f, h * 0.1f))
        drawRect(d, Offset(w * 0.205f, h * 0.24f), Size(w * 0.1f, h * 0.34f))
        drawCircle(d, h * 0.07f, Offset(w * 0.7f, h * 0.3f))
        drawCircle(d, h * 0.07f, Offset(w * 0.79f, h * 0.45f))
        drawCircle(d, h * 0.07f, Offset(w * 0.61f, h * 0.45f))
    }
}

val TitleOutline = Color(0xFF2E0C82)

private fun Double.em(fontSize: TextUnit): TextUnit = (fontSize.value * this).sp

/** Yellow progress bar on a dark track, with optional "320 / 500" text inside. */
@Composable
fun PvProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 14.dp,
    track: Color = Color(0xFF283062),
    label: String? = null,
) {
    Box(modifier.height(height).clip(Pill).background(track), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(Pill)
                .background(Brush.verticalGradient(listOf(Color(0xFFFFDD55), Color(0xFFFFA90F)))),
        ) {
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .clip(Pill)
                    .background(Color.White.copy(alpha = 0.35f)),
            )
        }
        if (label != null) {
            OutlinedText(label, (height.value * 0.55f).sp, outline = Color(0xFF9A4A00), outlineWidth = 1.2.dp, shadow = false)
        }
    }
}

/** Star badge sitting on the left end of an XP bar. */
@Composable
fun LevelBar(level: Int, xp: Int, goal: Int, modifier: Modifier = Modifier, height: Dp = 26.dp, showLabel: Boolean = true) {
    Box(modifier, contentAlignment = Alignment.CenterStart) {
        PvProgress(
            xp.toFloat() / goal,
            Modifier.fillMaxWidth().padding(start = height * 0.7f),
            height = height,
            track = Color(0xFF241A4F),
            label = if (showLabel) "$xp / $goal" else null,
        )
        StarBadge(level, height * 1.65f)
    }
}

@Composable
fun PvToggle(checked: Boolean, onChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val knob by animateDpAsState(if (checked) 32.dp else 2.dp, label = "knob")
    val fill = if (checked) Brush.verticalGradient(listOf(Color(0xFF5FE04A), Color(0xFF22B03A))) else SolidColor(Color(0xFFB8B2C8))
    Box(
        modifier
            .size(66.dp, 36.dp)
            .clip(Pill)
            .background(fill)
            .border(2.dp, if (checked) Color(0xFF17922F) else Color(0xFF8F88A3), Pill)
            .clickable { onChange(!checked) },
    ) {
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .offset(x = knob)
                .size(32.dp)
                .padding(2.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}

/** An arched ribbon banner with folded tails, drawn behind popup titles. */
@Composable
fun Ribbon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val dark = color.darken(0.62f)
        val light = color.copy(
            red = (color.red * 1.3f).coerceAtMost(1f),
            green = (color.green * 1.3f).coerceAtMost(1f),
            blue = (color.blue * 1.3f).coerceAtMost(1f),
        )
        val arch = h * 0.16f
        fun tail(left: Boolean) = Path().apply {
            fun x(f: Float) = if (left) w * f else w * (1 - f)
            moveTo(x(0.17f), h * 0.3f)
            lineTo(x(0f), h * 0.36f)
            lineTo(x(0.07f), h * 0.66f)
            lineTo(x(0f), h * 0.98f)
            lineTo(x(0.17f), h * 0.9f)
            close()
        }
        fun fold(left: Boolean) = Path().apply {
            fun x(f: Float) = if (left) w * f else w * (1 - f)
            moveTo(x(0.1f), h * 0.78f)
            lineTo(x(0.17f), h * 0.9f)
            lineTo(x(0.17f), h * 0.7f)
            close()
        }
        for (left in listOf(true, false)) {
            drawPath(tail(left), Brush.verticalGradient(listOf(color.darken(0.8f), dark)))
            drawPath(tail(left), dark.darken(0.6f), style = Stroke(h * 0.035f, join = StrokeJoin.Round))
        }
        val band = Path().apply {
            moveTo(w * 0.08f, h * 0.12f + arch)
            quadraticTo(w * 0.5f, h * 0.12f - arch, w * 0.92f, h * 0.12f + arch)
            lineTo(w * 0.92f, h * 0.78f + arch)
            quadraticTo(w * 0.5f, h * 0.78f - arch, w * 0.08f, h * 0.78f + arch)
            close()
        }
        drawPath(band, Brush.verticalGradient(listOf(light, color, dark), h * 0.0f, h * 0.9f))
        val shine = Path().apply {
            moveTo(w * 0.1f, h * 0.17f + arch)
            quadraticTo(w * 0.5f, h * 0.17f - arch, w * 0.9f, h * 0.17f + arch)
            lineTo(w * 0.9f, h * 0.29f + arch)
            quadraticTo(w * 0.5f, h * 0.29f - arch, w * 0.1f, h * 0.29f + arch)
            close()
        }
        drawPath(shine, Color.White.copy(alpha = 0.25f))
        drawPath(band, dark.darken(0.6f), style = Stroke(h * 0.035f, join = StrokeJoin.Round))
        drawPath(fold(true), dark.darken(0.5f))
        drawPath(fold(false), dark.darken(0.5f))
    }
}

/** Fat five-point gold star with a bevel, for flanking titles. */
@Composable
fun GoldStar(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val r = size.minDimension / 2
        val star = starPath(center, r, r * 0.5f)
        drawPath(star, PV.GoldEdge, style = Stroke(r * 0.22f, join = StrokeJoin.Round))
        drawPath(star, Brush.verticalGradient(listOf(Color(0xFFFFF3A0), Color(0xFFFFC21F), Color(0xFFFF9410))))
        drawPath(starPath(center.copy(y = center.y - r * 0.12f), r * 0.5f, r * 0.24f), Color.White.copy(alpha = 0.35f))
    }
}

/** A round or rounded-square icon badge with a glossy highlight, like the game's settings icons. */
@Composable
fun GlossyIcon(icon: ImageVector, top: Color, bottom: Color, size: Dp, modifier: Modifier = Modifier, round: Boolean = true) {
    val shape = if (round) CircleShape else RoundedCornerShape(size * 0.26f)
    Box(
        modifier
            .size(size)
            .shadow(4.dp, shape)
            .clip(shape)
            .background(bottom.darken(0.7f))
            .padding(bottom = 3.dp)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(top, bottom)))
            .border(2.dp, Color.White.copy(alpha = 0.55f), shape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = size * 0.08f)
                .size(size * 0.62f, size * 0.3f)
                .clip(Pill)
                .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0.05f)))),
        )
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(size * 0.58f))
    }
}

enum class PanelStyle { Purple, Dark }

/**
 * The rounded popup panel: a glowing rim around a glossy body. Purple for rewards,
 * dark navy with a gold-to-pink rim for profile and results.
 */
@Composable
fun Panel(style: PanelStyle, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    val inner = RoundedCornerShape(25.dp)
    val (bg, rim, glow) = when (style) {
        PanelStyle.Purple -> Triple(
            Brush.verticalGradient(listOf(Color(0xFF5A2DC0), Color(0xFF3A1A92), Color(0xFF2A1070))),
            Brush.verticalGradient(listOf(Color(0xFFD9C4FF), Color(0xFF9A6BFF), Color(0xFF6A3BE0))),
            Color(0xFFA98BFF),
        )
        PanelStyle.Dark -> Triple(
            Brush.verticalGradient(listOf(Color(0xFF3A2580), PV.Panel, PV.PanelDeep)),
            Brush.verticalGradient(listOf(Color(0xFFFFE08A), Color(0xFFFFA445), Color(0xFFFF5E8A))),
            Color(0xFFFF9A4A),
        )
    }
    Column(
        modifier
            .drawBehind {
                // Soft outer glow, like light spilling off the rim.
                for (i in 1..6) {
                    val spread = i * 2.5.dp.toPx()
                    drawRoundRect(
                        glow.copy(alpha = 0.16f / i),
                        Offset(-spread, -spread),
                        Size(size.width + spread * 2, size.height + spread * 2),
                        CornerRadius(28.dp.toPx() + spread),
                        style = Stroke(2.5.dp.toPx()),
                    )
                }
            }
            .shadow(18.dp, shape)
            .clip(shape)
            .background(rim)
            .padding(3.dp)
            .clip(inner)
            .background(bg)
            .border(1.5.dp, Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.35f), Color.Transparent, Color.Black.copy(alpha = 0.3f))), inner)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

/** A darker inset box inside a panel. */
@Composable
fun InnerBox(
    modifier: Modifier = Modifier,
    border: Brush = SolidColor(Color(0x557E57FF)),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier
            .clip(shape)
            .background(Brush.verticalGradient(listOf(PV.PanelInner.darken(0.85f), PV.PanelInner)))
            .border(1.5.dp, border, shape)
            .border(3.dp, Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.22f), Color.Transparent)), shape)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

/**
 * A popup with a big title that overlaps the top of its panel, like "Welcome BONUS" and "LEVEL UP!".
 * The title is [titleHeight] tall and reaches [overlap] down into the panel.
 */
@Composable
fun TitledPopup(
    style: PanelStyle,
    titleHeight: Dp,
    modifier: Modifier = Modifier,
    overlap: Dp = 44.dp,
    onClose: (() -> Unit)? = null,
    redClose: Boolean = true,
    title: @Composable BoxScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
        Panel(style, Modifier.fillMaxWidth().padding(top = titleHeight - overlap)) {
            Box(Modifier.height(overlap - 6.dp))
            content()
        }
        Box(Modifier.fillMaxWidth().height(titleHeight), contentAlignment = Alignment.Center, content = title)
        if (onClose != null) {
            CloseButton(
                onClose,
                Modifier.align(Alignment.TopEnd).padding(top = titleHeight - overlap - 18.dp).offset(x = 10.dp),
                red = redClose,
            )
        }
    }
}

/** Title lines stacked on a ribbon, with an optional crown on top. */
@Composable
fun RibbonTitle(
    lines: List<Pair<String, Brush>>,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    ribbon: Color? = PV.Purple,
    crown: Boolean = true,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    val c = Offset(size.width / 2, size.height * 0.55f)
                    drawCircle(Brush.radialGradient(listOf(Color(0x88FFC24A), Color.Transparent), c, size.width * 0.5f), size.width * 0.5f, c)
                },
        )
        Sparkles(Modifier.fillMaxSize())
        if (ribbon != null) {
            Ribbon(ribbon, Modifier.fillMaxWidth().fillMaxHeight(0.52f).graphicsLayer { scaleX = 1.12f })
        }
        if (crown) {
            GoldStar(Modifier.align(Alignment.TopStart).padding(start = 30.dp, top = 36.dp).size(34.dp).graphicsLayer { rotationZ = -14f })
            GoldStar(Modifier.align(Alignment.TopEnd).padding(end = 30.dp, top = 36.dp).size(34.dp).graphicsLayer { rotationZ = 14f })
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 6.dp)) {
            if (crown) Crown(Modifier.size(84.dp, 60.dp).offset(y = 14.dp))
            lines.forEach { (text, fill) ->
                OutlinedText(
                    text,
                    fontSize,
                    fill = fill,
                    outline = TitleOutline,
                    outlineWidth = 6.dp,
                    depth = 5.dp,
                    lineHeight = fontSize * 0.98f,
                    letterSpacing = (-0.5).sp,
                    maxLines = 1,
                )
            }
        }
    }
}

/** An art image cut from the reference designs. */
@Composable
fun Art(@DrawableRes res: Int, modifier: Modifier = Modifier, crop: Boolean = false) {
    Image(
        painterResource(res),
        contentDescription = null,
        modifier = modifier,
        contentScale = if (crop) ContentScale.Crop else ContentScale.Fit,
    )
}

/** The soft-focus game room behind every screen, dimmed by [dim] so content stays readable. */
@Composable
fun RoomBackdrop(modifier: Modifier = Modifier, dim: Color = Color.Transparent) {
    Box(modifier) {
        Image(
            painterResource(R.drawable.bg_room),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
        )
        Box(Modifier.matchParentSize().background(dim))
    }
}

/** Light rays fanning out from the centre, slowly turning, behind prize art. */
@Composable
fun Sunburst(modifier: Modifier = Modifier, color: Color = Color(0xFFFFD86B)) {
    val turn by rememberInfiniteTransition(label = "rays").animateFloat(
        0f,
        360f,
        infiniteRepeatable(tween(40_000, easing = LinearEasing)),
        label = "turn",
    )
    Canvas(modifier) {
        val r = size.maxDimension * 0.75f
        drawCircle(Brush.radialGradient(listOf(color.copy(alpha = 0.75f), color.copy(alpha = 0.2f), Color.Transparent), center, r * 0.7f), r * 0.7f)
        rotate(turn) {
            for (i in 0 until 16) {
                val a0 = (i * 22.5 - 5) * PI / 180
                val a1 = (i * 22.5 + 5) * PI / 180
                val ray = Path().apply {
                    moveTo(center.x, center.y)
                    lineTo(center.x + (r * cos(a0)).toFloat(), center.y + (r * sin(a0)).toFloat())
                    lineTo(center.x + (r * cos(a1)).toFloat(), center.y + (r * sin(a1)).toFloat())
                    close()
                }
                drawPath(ray, Brush.radialGradient(listOf(color.copy(alpha = 0.32f), Color.Transparent), center, r * 0.85f))
            }
        }
    }
}

/** Little four-point twinkles scattered around a title or prize. */
@Composable
fun Sparkles(modifier: Modifier = Modifier, color: Color = Color(0xFFFFE27A)) {
    val pulse by rememberInfiniteTransition(label = "twinkle").animateFloat(
        0.6f,
        1f,
        infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse",
    )
    Canvas(modifier) {
        listOf(
            Triple(0.08f, 0.3f, 0.05f), Triple(0.92f, 0.22f, 0.06f), Triple(0.16f, 0.82f, 0.035f),
            Triple(0.86f, 0.78f, 0.04f), Triple(0.3f, 0.06f, 0.03f), Triple(0.72f, 0.04f, 0.035f),
        ).forEachIndexed { i, (x, y, s) ->
            val c = Offset(size.width * x, size.height * y)
            val r = size.width * s * if (i % 2 == 0) pulse else 1.6f - pulse
            val star = Path().apply {
                moveTo(c.x, c.y - r)
                quadraticTo(c.x, c.y, c.x + r * 0.7f, c.y)
                quadraticTo(c.x, c.y, c.x, c.y + r)
                quadraticTo(c.x, c.y, c.x - r * 0.7f, c.y)
                quadraticTo(c.x, c.y, c.x, c.y - r)
                close()
            }
            drawPath(star, if (i % 3 == 2) Color(0xFFD9B8FF) else color)
        }
    }
}
