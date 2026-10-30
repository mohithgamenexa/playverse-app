package com.playverse.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.playverse.app.data.DrawnArt
import com.playverse.app.data.Game
import com.playverse.app.ui.theme.Fredoka
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** A game's cover art: the image when there is one, otherwise a drawn stand-in. */
@Composable
fun GameArt(game: Game, modifier: Modifier = Modifier) {
    val art = game.art
    if (art != null) {
        Image(painterResource(art), game.name, modifier, contentScale = ContentScale.Crop, alignment = Alignment.TopCenter)
    } else {
        DrawnGameArt(game.drawn ?: DrawnArt.HexaBlock, modifier)
    }
}

@Composable
private fun DrawnGameArt(kind: DrawnArt, modifier: Modifier) {
    val measurer = rememberTextMeasurer()
    Canvas(modifier) {
        when (kind) {
            DrawnArt.JewelSlide -> jewels()
            DrawnArt.Arrows -> arrows()
            DrawnArt.HexaBlock -> hexagons()
            DrawnArt.PicQuiz -> {
                picQuiz()
                val q = measurer.measure("?", TextStyle(fontFamily = Fredoka, fontWeight = FontWeight.Bold, fontSize = (size.height / 3.2f).toSp(), color = Color(0xFF7A2A00)))
                val c = Offset(size.width * 0.74f, size.height * 0.46f)
                drawText(q, topLeft = Offset(c.x - q.size.width / 2f, c.y - q.size.height / 2f))
            }
        }
        // Soft sparkle light, like the painted art
        drawCircle(
            Brush.radialGradient(listOf(Color(0x55FFFFFF), Color.Transparent), Offset(size.width * 0.2f, size.height * 0.15f), size.width * 0.35f),
            size.width * 0.35f,
            Offset(size.width * 0.2f, size.height * 0.15f),
        )
    }
}

private fun DrawScope.background(light: Color, mid: Color, dark: Color) =
    drawRect(Brush.radialGradient(listOf(light, mid, dark), Offset(size.width * 0.5f, size.height * 0.35f), size.width * 0.8f))

private fun DrawScope.jewels() {
    background(Color(0xFFE05AD6), Color(0xFF8E2A9C), Color(0xFF4A1060))
    val colors = listOf(
        Color(0xFFFF3B5C) to Color(0xFFB0102E),
        Color(0xFF39A8FF) to Color(0xFF1546B8),
        Color(0xFF4CE07A) to Color(0xFF128A3A),
        Color(0xFFFFD43B) to Color(0xFFE08A00),
        Color(0xFFB66BFF) to Color(0xFF5B1FB8),
    )
    val r = size.height * 0.17f
    for (row in 0..1) for (col in 0..4) {
        val cx = size.width * (0.12f + col * 0.19f) + if (row == 1) size.width * 0.09f else 0f
        val cy = size.height * (0.3f + row * 0.36f)
        if (cx > size.width * 0.95f) continue
        val (light, dark) = colors[(row * 2 + col) % colors.size]
        val gem = Path().apply {
            moveTo(cx - r * 0.55f, cy - r * 0.55f)
            lineTo(cx + r * 0.55f, cy - r * 0.55f)
            lineTo(cx + r, cy - r * 0.1f)
            lineTo(cx, cy + r)
            lineTo(cx - r, cy - r * 0.1f)
            close()
        }
        drawPath(gem, Brush.verticalGradient(listOf(light, dark), cy - r, cy + r))
        drawPath(gem, Color.White.copy(alpha = 0.6f), style = Stroke(r * 0.08f, join = StrokeJoin.Round))
        drawLine(Color.White.copy(alpha = 0.5f), Offset(cx - r, cy - r * 0.1f), Offset(cx + r, cy - r * 0.1f), r * 0.06f)
        drawCircle(Color.White.copy(alpha = 0.8f), r * 0.12f, Offset(cx - r * 0.25f, cy - r * 0.32f))
    }
}

private fun DrawScope.arrows() {
    background(Color(0xFF3FD6C8), Color(0xFF128C8C), Color(0xFF0A4F5A))
    val cell = size.height / 4.2f
    val startX = (size.width - cell * 6) / 2
    for (row in 0..3) for (col in 0..5) {
        drawCircle(Color.White.copy(alpha = 0.25f), cell * 0.07f, Offset(startX + cell * (col + 0.5f), cell * (row + 0.6f)))
    }
    val arrows = listOf(
        Triple(Offset(0.5f, 0.6f), 0f, Color(0xFFFF4D6D)),
        Triple(Offset(2.5f, 1.6f), 90f, Color(0xFFFFD43B)),
        Triple(Offset(4.5f, 0.6f), 180f, Color(0xFF7E57FF)),
        Triple(Offset(1.5f, 2.6f), 270f, Color(0xFF4CE07A)),
        Triple(Offset(4.5f, 2.6f), 0f, Color(0xFFFF9A2E)),
        Triple(Offset(3.5f, 3.6f), 180f, Color(0xFF39A8FF)),
    )
    arrows.forEach { (p, angle, color) ->
        val c = Offset(startX + cell * p.x, cell * p.y)
        rotate(angle, c) {
            val shaft = cell * 0.9f
            drawLine(Color(0x55000000), Offset(c.x - shaft / 2, c.y + 3), Offset(c.x + shaft / 2, c.y + 3), cell * 0.28f, StrokeCap.Round)
            drawLine(color, Offset(c.x - shaft / 2, c.y), Offset(c.x + shaft / 2, c.y), cell * 0.28f, StrokeCap.Round)
            val head = Path().apply {
                moveTo(c.x + shaft / 2 + cell * 0.32f, c.y)
                lineTo(c.x + shaft / 2 - cell * 0.12f, c.y - cell * 0.34f)
                lineTo(c.x + shaft / 2 - cell * 0.12f, c.y + cell * 0.34f)
                close()
            }
            drawPath(head, color)
            drawPath(head, Color.White.copy(alpha = 0.7f), style = Stroke(cell * 0.05f, join = StrokeJoin.Round))
        }
    }
}

private fun DrawScope.hexagons() {
    background(Color(0xFF6FB8FF), Color(0xFF2A6FD6), Color(0xFF173E8C))
    val r = size.height * 0.15f
    val w = r * 1.732f
    val colors = listOf(Color(0xFFFF4D6D), Color(0xFFFFD43B), Color(0xFF4CE07A), Color(0xFFB66BFF), Color(0xFFFF9A2E), Color(0xFF39D8FF))
    var i = 0
    for (row in 0..3) {
        val y = size.height * 0.16f + row * r * 1.5f
        val offset = if (row % 2 == 1) w / 2 else 0f
        var x = (size.width - w * 6) / 2 + offset
        repeat(6) {
            val hex = Path().apply {
                for (k in 0..5) {
                    val a = PI / 180 * (60 * k - 30)
                    val px = x + w / 2 + (r * 0.95f * cos(a)).toFloat()
                    val py = y + (r * 0.95f * sin(a)).toFloat()
                    if (k == 0) moveTo(px, py) else lineTo(px, py)
                }
                close()
            }
            val filled = (row + it * 2 + i) % 3 != 0
            if (filled) {
                val c = colors[(row * 7 + it * 3) % colors.size]
                drawPath(hex, Brush.verticalGradient(listOf(c, c.copy(red = c.red * 0.7f, green = c.green * 0.7f, blue = c.blue * 0.7f)), y - r, y + r))
                drawPath(hex, Color.White.copy(alpha = 0.55f), style = Stroke(r * 0.08f, join = StrokeJoin.Round))
            } else {
                drawPath(hex, Color(0x33001040))
                drawPath(hex, Color.White.copy(alpha = 0.18f), style = Stroke(r * 0.05f, join = StrokeJoin.Round))
            }
            x += w
        }
        i++
    }
}

private fun DrawScope.picQuiz() {
    background(Color(0xFFFFB067), Color(0xFFF0603A), Color(0xFF9C2A3A))
    fun photo(c: Offset, angle: Float, sky: Color) = rotate(angle, c) {
        val s = Size(size.height * 0.62f, size.height * 0.52f)
        val tl = Offset(c.x - s.width / 2, c.y - s.height / 2)
        drawRoundRect(Color(0x44000000), tl + Offset(4f, 6f), s, CornerRadius(12f))
        drawRoundRect(Color.White, tl, s, CornerRadius(12f))
        val inner = Size(s.width * 0.86f, s.height * 0.7f)
        val itl = Offset(c.x - inner.width / 2, tl.y + s.width * 0.07f)
        drawRect(sky, itl, inner)
        val hill = Path().apply {
            moveTo(itl.x, itl.y + inner.height)
            lineTo(itl.x + inner.width * 0.35f, itl.y + inner.height * 0.35f)
            lineTo(itl.x + inner.width * 0.6f, itl.y + inner.height * 0.7f)
            lineTo(itl.x + inner.width * 0.78f, itl.y + inner.height * 0.5f)
            lineTo(itl.x + inner.width, itl.y + inner.height)
            close()
        }
        drawPath(hill, Color(0xFF3FAE5A))
        drawCircle(Color(0xFFFFE45C), inner.height * 0.13f, Offset(itl.x + inner.width * 0.78f, itl.y + inner.height * 0.25f))
    }
    photo(Offset(size.width * 0.24f, size.height * 0.52f), -10f, Color(0xFF7FD3FF))
    photo(Offset(size.width * 0.44f, size.height * 0.46f), 6f, Color(0xFFB79BFF))
    val c = Offset(size.width * 0.74f, size.height * 0.46f)
    drawCircle(Color(0xFFB0560A), size.height * 0.27f, c)
    drawCircle(Brush.verticalGradient(listOf(Color(0xFFFFE877), Color(0xFFFFB300)), c.y - size.height * 0.25f, c.y + size.height * 0.25f), size.height * 0.245f, c)
}
