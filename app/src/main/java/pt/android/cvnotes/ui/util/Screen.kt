package pt.android.cvnotes.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector = Icons.Default.DisabledByDefault,
    var content: @Composable () -> Unit = {}
    ) {
    data object Default: Screen("default")

    data object Splash: Screen("splash_screen")

    data object Auth: Screen("auth")
    data object Intro: Screen("intro_screen")
    data object Register: Screen("register_screen")
    data object Login: Screen("login_screen")

    data object Home: Screen("home")
    data object Dashboard: Screen("dash_screen", "Dashboard", Icons.Filled.Notes)
    data object About: Screen("about_screen", "About", Icons.Filled.Info)
    data object SectionDetails: Screen("section_details_screen", "Section Details")
    data object NewNote: Screen("new_note_screen", "New Note")
    data object EditNote: Screen("edit_note_screen", "Edit Note")

    companion object {
        val Defaults = listOf(
            Default,
            Default,
            Default,
            Default
        )
    }
}
