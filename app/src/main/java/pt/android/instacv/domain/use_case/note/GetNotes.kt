package pt.android.instacv.domain.use_case.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.android.instacv.domain.model.Note
import pt.android.instacv.domain.repository.NoteRepository
import pt.android.instacv.domain.util.NoteSection

class GetNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(
        section: NoteSection = NoteSection.ALL
    ): Flow<List<Note>> {
        return noteRepository.getNotes().map { notes ->
            when (section) {
                NoteSection.ALL -> notes
                else -> notes.filter { section.id == it.section }
            }
        }
    }
}