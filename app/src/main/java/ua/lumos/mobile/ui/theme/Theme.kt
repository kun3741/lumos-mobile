package ua.lumos.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Paper,
    primaryContainer = Midnight,
    onPrimaryContainer = Paper,
    secondary = Sunlight,
    onSecondary = Midnight,
    secondaryContainer = ColorTokens.LightAmberContainer,
    onSecondaryContainer = Midnight,
    tertiary = WarmRed,
    onTertiary = Paper,
    background = Cloud,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = Slate,
    outline = ColorTokens.LightOutline
)

private val DarkColors = darkColorScheme(
    primary = ElectricBlueDark,
    onPrimary = MidnightDark,
    primaryContainer = SurfaceRaisedDark,
    onPrimaryContainer = InkDark,
    secondary = SunlightDark,
    onSecondary = MidnightDark,
    secondaryContainer = ColorTokens.DarkAmberContainer,
    onSecondaryContainer = InkDark,
    tertiary = WarmRedDark,
    onTertiary = MidnightDark,
    background = MidnightDark,
    onBackground = InkDark,
    surface = SurfaceDark,
    onSurface = InkDark,
    surfaceVariant = SurfaceRaisedDark,
    onSurfaceVariant = SlateDark,
    outline = ColorTokens.DarkOutline
)

private object ColorTokens {
    val LightAmberContainer = androidx.compose.ui.graphics.Color(0xFFFFE7A8)
    val DarkAmberContainer = androidx.compose.ui.graphics.Color(0xFF4C3B10)
    val LightOutline = androidx.compose.ui.graphics.Color(0xFF788393)
    val DarkOutline = androidx.compose.ui.graphics.Color(0xFF8995A8)
}

private val LumosShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun LumosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = LumosTypography,
        shapes = LumosShapes,
        content = content
    )
}
