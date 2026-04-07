package pt.rvcoding.cvnotes.appfunctions

import androidx.navigation.NavController
import pt.rvcoding.cvnotes.ui.util.Screen
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Queues a navigation action to be applied when [NavController] is on the Home graph
 * (see [tryConsume] from [androidx.navigation.NavController.OnDestinationChangedListener]).
 */
sealed class PendingNav {
    data object Dashboard : PendingNav()

    data class SectionDetails(val sectionId: Int) : PendingNav()

    data class NewNote(val sectionId: Int) : PendingNav()

    data class EditNote(val sectionId: Int, val noteId: Long) : PendingNav()
}

@Singleton
class PendingNavigationHolder @Inject constructor() {

    private val lock = Any()

    @Volatile
    private var pending: PendingNav? = null

    fun setPending(nav: PendingNav) {
        synchronized(lock) { pending = nav }
    }

    /**
     * Applies a pending navigation if one exists. Safe to call repeatedly; clears pending before navigating.
     */
    fun tryConsume(navController: NavController) {
        val toRun: PendingNav
        synchronized(lock) {
            toRun = pending ?: return
            pending = null
        }
        try {
            when (toRun) {
                PendingNav.Dashboard -> {
                    navController.navigate(Screen.Dashboard.route) {
                        launchSingleTop = true
                    }
                }
                is PendingNav.SectionDetails -> {
                    navController.navigate("${Screen.SectionDetails.route}/${toRun.sectionId}")
                }
                is PendingNav.NewNote -> {
                    navController.navigate("${Screen.NewNote.route}/${toRun.sectionId}")
                }
                is PendingNav.EditNote -> {
                    navController.navigate("${Screen.EditNote.route}/${toRun.sectionId}/${toRun.noteId}")
                }
            }
        } catch (_: Exception) {
            // If navigation fails (e.g. invalid route), drop pending; user can retry from UI.
        }
    }
}
