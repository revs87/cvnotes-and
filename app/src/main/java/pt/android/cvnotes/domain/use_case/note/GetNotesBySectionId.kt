package pt.android.cvnotes.domain.use_case.note

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.repository.NoteRepository

class GetNotesBySectionId(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(
        sectionId: Int
    ): Flow<List<Note>> {
        return noteRepository.getNotes(sectionId)
    }
}