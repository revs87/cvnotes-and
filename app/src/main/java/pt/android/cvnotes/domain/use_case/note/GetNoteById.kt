package pt.android.cvnotes.domain.use_case.note

import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.repository.NoteRepository

class GetNoteById(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? {
        return noteRepository.getNoteById(id)
    }
}