package pt.rvcoding.cvnotes.domain.use_case.note

import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.repository.NoteRepository

class InsertNote(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        noteRepository.insertNote(note)
    }
}