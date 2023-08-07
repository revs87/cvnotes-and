package pt.android.instacv.domain.use_case.note

import pt.android.instacv.domain.model.Note
import pt.android.instacv.domain.repository.NoteRepository

class DeleteNote(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        noteRepository.deleteNote(note)
    }
}