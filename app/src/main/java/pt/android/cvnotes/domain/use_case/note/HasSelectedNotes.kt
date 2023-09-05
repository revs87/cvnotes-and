package pt.android.cvnotes.domain.use_case.note

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.domain.repository.NoteRepository

class HasSelectedNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(sectionId: Int): Flow<Boolean> {
        return noteRepository.hasSelectedNotes(sectionId)
    }
}