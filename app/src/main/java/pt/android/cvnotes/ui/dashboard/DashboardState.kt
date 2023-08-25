package pt.android.cvnotes.ui.dashboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.android.cvnotes.domain.model.SectionWithNotes

data class DashboardState(
    val sectionsWithNotes: Flow<List<SectionWithNotes>> = flow { emptyList<SectionWithNotes>() },
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)