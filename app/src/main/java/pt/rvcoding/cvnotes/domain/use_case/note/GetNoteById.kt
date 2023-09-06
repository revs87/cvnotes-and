package pt.rvcoding.cvnotes.domain.use_case.note

import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.repository.NoteRepository

class GetNoteById(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? {
        return noteRepository.getNoteById(id)
    }
}