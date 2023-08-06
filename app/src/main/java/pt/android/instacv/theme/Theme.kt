package pt.android.instacv.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorSchemePalette = darkColorScheme(
    primary = Blue200,
    primaryContainer = Blue700,
    secondary = Green300,
    background = Gray500,
    surface = Gray500,
    surfaceVariant = Gray300,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
)

private val LightColorSchemePalette = lightColorScheme(
    primary = Blue500,
    primaryContainer = Blue700,
    secondary = Green300,
    background = White,
    surface = White,
    surfaceVariant = Gray300,
    onPrimary = White,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
)

@Composable
fun MyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) {
        DarkColorSchemePalette
    } else {
        LightColorSchemePalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}