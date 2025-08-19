package pt.rvcoding.cvnotes.ui.section_details

import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.Section


data class SectionDetailsState(
    val section: Section = Section.Default,
    val notes: List<Note> = emptyList(),
    val hasSelectedNotes: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)