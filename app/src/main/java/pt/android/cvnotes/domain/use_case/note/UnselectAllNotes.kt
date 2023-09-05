package pt.android.cvnotes.domain.use_case.note

import pt.android.cvnotes.domain.repository.NoteRepository

class UnselectAllNotes(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(sectionId: Int) {
        noteRepository.unselectAllNotes(sectionId)
    }
}