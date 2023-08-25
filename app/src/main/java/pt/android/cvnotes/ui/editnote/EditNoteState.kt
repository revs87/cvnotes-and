package pt.android.cvnotes.ui.editnote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.android.cvnotes.domain.model.Note


data class EditNoteState(
    val note: Flow<Note?> = flow { emit(Note.default) },
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)