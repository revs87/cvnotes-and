package pt.android.cvnotes.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector = Icons.Default.DisabledByDefault,
    ) {
    object Default: Screen("default", "title", Icons.Default.DisabledByDefault)

    object Splash: Screen("splash_screen")
    object About: Screen("about_screen", "About")

    object Auth: Screen("auth")
    object Intro: Screen("intro_screen")
    object Register: Screen("register_screen")
    object Login: Screen("login_screen")

    object Home: Screen("home")
    object Dashboard: Screen("dash_screen")
}
