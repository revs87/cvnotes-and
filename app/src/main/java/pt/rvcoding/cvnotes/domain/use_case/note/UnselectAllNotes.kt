package pt.rvcoding.cvnotes.domain.use_case.note

import pt.rvcoding.cvnotes.domain.repository.NoteRepository

class UnselectAllNotes(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(sectionId: Int) {
        noteRepository.unselectAllNotes(sectionId)
    }
}