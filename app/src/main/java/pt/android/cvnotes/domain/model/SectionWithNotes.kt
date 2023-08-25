package pt.android.cvnotes.domain.model

import kotlinx.coroutines.flow.Flow

data class SectionWithNotes(
    val section: Section,
    val notes: List<Note>
)
