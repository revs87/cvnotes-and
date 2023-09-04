package pt.android.cvnotes.ui.editnote

import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.Section


data class EditNoteState(
    val section: Section? = null,
    val note: Note? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)