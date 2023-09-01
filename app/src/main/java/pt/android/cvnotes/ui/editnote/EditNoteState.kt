package pt.android.cvnotes.ui.editnote

import pt.android.cvnotes.domain.model.Note


data class EditNoteState(
    val note: Note? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)