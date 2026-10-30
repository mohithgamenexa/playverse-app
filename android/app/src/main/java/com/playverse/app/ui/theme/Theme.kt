package com.playverse.app.ui.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.playverse.app.R

/** The colors used across Playverse, picked from the reference designs. */
object PV {
    val Navy = Color(0xFF1C2457)
    val TextMuted = Color(0xFF515A82)
    val Cream = Color(0xFFFBF1E1)
    val CardCream = Color(0xFFFFF8EE)
    val CardBorder = Color(0xFFF0DFC4)
    val RewardBox = Color(0xFFFCE9CB)

    val NavBarTop = Color(0xFF263E72)
    val NavBarBottom = Color(0xFF12224A)

    val Purple = Color(0xFF4B23B0)
    val PurpleDeep = Color(0xFF2E157A)
    val PurpleBright = Color(0xFF7E57FF)
    val PurpleOutline = Color(0xFF3A1478)
    val Lavender = Color(0xFFC9B6FF)

    val Panel = Color(0xFF2D1E5C)
    val PanelDeep = Color(0xFF1D1342)
    val PanelInner = Color(0x66140A3A)
    val PanelGold = Color(0xFFF2A444)

    val Gold = Color(0xFFFFD43B)
    val GoldLight = Color(0xFFFFE45C)
    val GoldDeep = Color(0xFFFFA41B)
    val GoldEdge = Color(0xFFB8620A)

    val ScrimTop = Color(0xCC1A0D3D)
}

val Fredoka = FontFamily(
    Font(R.font.fredoka_medium, FontWeight.Medium),
    Font(R.font.fredoka_semibold, FontWeight.SemiBold),
    Font(R.font.fredoka_bold, FontWeight.Bold),
)

@Composable
fun PlayverseTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = lightColorScheme(primary = PV.Purple, surface = PV.CardCream)) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(fontFamily = Fredoka, fontWeight = FontWeight.SemiBold, color = PV.Navy),
            content = content,
        )
    }
}
