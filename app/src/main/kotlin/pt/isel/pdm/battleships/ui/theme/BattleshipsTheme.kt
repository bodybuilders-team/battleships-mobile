package pt.isel.pdm.battleships.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/**
 * Create a [BattleshipsTheme] that is based on the system's current theme.
 *
 * @param darkTheme whether the theme should be dark or light
 * @param content the content to be displayed
 */
@Composable
fun BattleshipsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private val DarkColorPalette = darkColors(
    primary = LightBlue,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = DarkBlue,
    primaryVariant = Purple700,
    secondary = Teal200
)
