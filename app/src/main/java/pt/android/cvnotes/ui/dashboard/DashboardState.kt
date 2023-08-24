package pt.android.cvnotes.ui.dashboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.android.cvnotes.domain.model.Note

data class DashboardState(
    val notes: Flow<List<Note>> = flow { },
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)