package pt.android.cvnotes.ui.section_details

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.Section


data class SectionDetailsState(
    val section: Section = Section.Default,
    val notes: Flow<List<Note>> = flow { emit(emptyList()) },
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)