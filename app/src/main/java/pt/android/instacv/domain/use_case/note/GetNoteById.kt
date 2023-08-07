package pt.android.instacv.domain.use_case.note

import pt.android.instacv.domain.model.Note
import pt.android.instacv.domain.repository.NoteRepository

class GetNoteById(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return noteRepository.getNoteById(id)
    }
}