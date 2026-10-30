package com.playverse.app.ui.components

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.graphicsLayer
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

/** Chunky cartoon text: a thick outline behind a solid or gradient fill. */
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
    Box(modifier) {
        Text(
            text,
            maxLines = maxLines,
            style = base.copy(
                color = outline,
                drawStyle = Stroke(width = strokePx, join = StrokeJoin.Round),
                shadow = if (shadow) Shadow(Color(0x55000000), Offset(0f, strokePx * 0.6f), strokePx) else null,
            ),
        )
        Text(text, maxLines = maxLines, style = base.copy(brush = fill))
    }
}

val GoldFill = Brush.verticalGradient(listOf(PV.GoldLight, PV.GoldDeep))
val WhiteFill = Brush.verticalGradient(listOf(Color.White, Color(0xFFE2D6FF)))

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
            .shadow(4.dp, Pill)
            .clip(Pill)
            .background(s.edge)
            .clickable(source, indication = null, enabled = enabled, onClick = onClick)
            .padding(bottom = 4.dp)
            .clip(Pill)
            .background(Brush.verticalGradient(listOf(s.top, s.bottom))),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp)
                .fillMaxWidth(0.86f)
                .fillMaxHeight(0.4f)
                .clip(Pill)
                .background(Color.White.copy(alpha = 0.22f)),
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
        OutlinedText(text, fontSize, fill = SolidColor(style.text), outline = outline, outlineWidth = 1.5.dp, shadow = false, lineHeight = lineHeight)
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

/** The Playverse wordmark: crown, "Play" in gold, "verse" in white, gamepad underneath. */
@Composable
fun PlayverseLogo(fontSize: TextUnit, modifier: Modifier = Modifier) {
    val unit = with(LocalDensity.current) { fontSize.toDp() }
    Box(modifier, contentAlignment = Alignment.TopCenter) {
        Crown(Modifier.size(unit * 0.95f, unit * 0.7f))
        Row(Modifier.padding(top = unit * 0.42f)) {
            OutlinedText("Play", fontSize, fill = GoldFill, outlineWidth = unit * 0.09f, letterSpacing = (-0.02).em(fontSize))
            OutlinedText("verse", fontSize, fill = WhiteFill, outlineWidth = unit * 0.09f, letterSpacing = (-0.02).em(fontSize))
        }
        Image(
            painterResource(R.drawable.nav_games),
            contentDescription = null,
            modifier = Modifier
                .padding(top = unit * 1.45f)
                .offset(x = unit * 0.08f)
                .size(unit * 0.62f, unit * 0.5f),
        )
    }
}

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

/** A purple ribbon band with tails, drawn behind popup titles. */
@Composable
fun Ribbon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val dark = color.copy(red = color.red * 0.62f, green = color.green * 0.62f, blue = color.blue * 0.62f)
        fun tail(left: Boolean) = Path().apply {
            val x0 = if (left) 0f else w
            val x1 = if (left) w * 0.16f else w * 0.84f
            moveTo(x0, h * 0.28f)
            lineTo(x1, h * 0.18f)
            lineTo(x1, h * 0.86f)
            lineTo(x0, h * 0.96f)
            lineTo(if (left) w * 0.06f else w * 0.94f, h * 0.62f)
            close()
        }
        drawPath(tail(true), dark)
        drawPath(tail(false), dark)
        drawRoundRect(
            Brush.verticalGradient(listOf(color, dark)),
            Offset(w * 0.08f, h * 0.06f),
            Size(w * 0.84f, h * 0.74f),
            CornerRadius(h * 0.2f),
        )
    }
}

enum class PanelStyle { Purple, Dark }

/** The rounded popup panel. Purple for rewards, dark navy with a gold rim for profile and results. */
@Composable
fun Panel(style: PanelStyle, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    val (bg, rim) = when (style) {
        PanelStyle.Purple -> Brush.verticalGradient(listOf(Color(0xFF4A24A8), Color(0xFF2E157A))) to
            Brush.verticalGradient(listOf(PV.PurpleBright, Color(0xFF5B2FD0)))
        PanelStyle.Dark -> Brush.verticalGradient(listOf(PV.Panel, PV.PanelDeep)) to
            Brush.verticalGradient(listOf(Color(0xFFFFC266), Color(0xFFE0702A)))
    }
    Column(
        modifier
            .shadow(16.dp, shape)
            .clip(shape)
            .background(bg)
            .border(3.dp, rim, shape)
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
        modifier.clip(shape).background(PV.PanelInner).border(1.5.dp, border, shape).padding(12.dp),
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
        if (ribbon != null) Ribbon(ribbon, Modifier.fillMaxWidth().fillMaxHeight(0.5f))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 6.dp)) {
            if (crown) Crown(Modifier.size(64.dp, 46.dp).offset(y = 10.dp))
            lines.forEach { (text, fill) ->
                OutlinedText(
                    text,
                    fontSize,
                    fill = fill,
                    outlineWidth = 4.dp,
                    lineHeight = fontSize * 1.0f,
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

/** Soft warm glows behind the top of the home screen, standing in for the cosy room background. */
fun Modifier.roomGlow(): Modifier = drawBehind {
    drawRect(
        Brush.verticalGradient(
            0f to Color(0xFF4E2462),
            0.45f to Color(0xFF9C4A5E),
            0.8f to Color(0xFFDB8558),
            1f to Color(0xFFF0B07A),
        ),
    )
    listOf(
        Triple(0.12f, 0.25f, Color(0x55FFD27A)),
        Triple(0.88f, 0.18f, Color(0x44FF9EC4)),
        Triple(0.5f, 0.05f, Color(0x3390B8FF)),
        Triple(0.3f, 0.75f, Color(0x44FFE0A0)),
    ).forEach { (x, y, c) ->
        val center = Offset(size.width * x, size.height * y)
        drawCircle(Brush.radialGradient(listOf(c, Color.Transparent), center, size.width * 0.4f), size.width * 0.4f, center)
    }
}
