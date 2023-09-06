package pt.rvcoding.cvnotes.domain.use_case.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.repository.NoteRepository
import pt.rvcoding.cvnotes.domain.util.SectionType

class GetNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(
        section: SectionType = SectionType.ALL
    ): Flow<List<Note>> {
        return noteRepository.getNotes(section.typeId).map { notes ->
            when (section) {
                SectionType.ALL -> notes
                else -> notes.filter { section.typeId == it.sectionId }
            }
        }
    }
}