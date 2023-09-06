package pt.rvcoding.cvnotes.ui.editnote

import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.Section


data class EditNoteState(
    val section: Section? = null,
    val note: Note? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)