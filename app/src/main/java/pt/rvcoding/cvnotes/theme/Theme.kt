package pt.rvcoding.cvnotes.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorSchemePalette = darkColorScheme(
    primary = Blue200,
    primaryContainer = Blue700,
    secondary = Green300,
    background = Gray300,
    surface = Gray300,
    surfaceVariant = Gray200,
    onPrimary = Green500,
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
    surfaceVariant = Gray100,
    onPrimary = White,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
)

@Composable
fun MyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colorScheme = if (darkTheme) {
//        DarkColorSchemePalette
//    } else {
//        LightColorSchemePalette
//    }

    val colorScheme = LightColorSchemePalette

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}