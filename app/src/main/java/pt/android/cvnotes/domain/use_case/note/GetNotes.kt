package pt.android.cvnotes.domain.use_case.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.repository.NoteRepository
import pt.android.cvnotes.domain.util.SectionType

class GetNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(
        section: SectionType = SectionType.ALL
    ): Flow<List<Note>> {
        return noteRepository.getNotes(section.id).map { notes ->
            when (section) {
                SectionType.ALL -> notes
                else -> notes.filter { section.id == it.sectionId }
            }
        }
    }
}