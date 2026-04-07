package pt.rvcoding.cvnotes.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SectionWithNotes(
    val section: Section,
    val notes: List<Note>
)
