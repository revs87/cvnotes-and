package pt.android.cvnotes.domain.model

data class SectionWithNotes(
    val section: Section,
    val notes: List<Note>
)
