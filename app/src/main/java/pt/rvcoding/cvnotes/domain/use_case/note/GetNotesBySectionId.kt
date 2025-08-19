package pt.rvcoding.cvnotes.domain.use_case.note

import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.repository.NoteRepository

class GetNotesBySectionId(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(
        sectionId: Int
    ): List<Note> {
        return noteRepository.getNotesList(sectionId)
    }
}