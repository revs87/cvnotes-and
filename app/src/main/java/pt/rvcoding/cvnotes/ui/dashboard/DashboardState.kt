package pt.rvcoding.cvnotes.ui.dashboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.rvcoding.cvnotes.domain.model.SectionWithNotes

data class DashboardState(
    val sectionsWithNotes: Flow<List<SectionWithNotes>> = flow { emit(emptyList()) },
    val sectionsHasSelected: Flow<Boolean> = flow { emit(false) },
    val scrollToBottom: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)