package pt.rvcoding.cvnotes.domain.use_case.note

import pt.rvcoding.cvnotes.domain.repository.NoteRepository

class HasSelectedNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(sectionId: Int): Boolean {
        return noteRepository.hasSelectedNotes(sectionId)
    }
}