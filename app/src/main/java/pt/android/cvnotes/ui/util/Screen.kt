package pt.android.cvnotes.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector = Icons.Default.DisabledByDefault,
    var content: @Composable () -> Unit = {}
    ) {
    data object Default: Screen("default", "title", Icons.Default.DisabledByDefault)

    data object Splash: Screen("splash_screen")
    data object Auth: Screen("auth")

    data object Intro: Screen("intro_screen")
    data object Register: Screen("register_screen")
    data object Login: Screen("login_screen")

    data object Home: Screen("home")
    data object Dashboard: Screen("dash_screen", "Dashboard")
    data object About: Screen("about_screen", "About")

    companion object {
        val Defaults = listOf(
            Default,
            Default,
            Default,
            Default
        )
    }
}
