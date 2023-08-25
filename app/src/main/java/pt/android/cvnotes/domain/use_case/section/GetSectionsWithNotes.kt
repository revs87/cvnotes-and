package pt.android.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.singleOrNull
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.model.SectionWithNotes
import pt.android.cvnotes.domain.repository.NoteRepository
import pt.android.cvnotes.domain.repository.SectionRepository

class GetSectionsWithNotes(
    private val sectionRepository: SectionRepository,
    private val noteRepository: NoteRepository,
) {
    operator fun invoke(): Flow<List<SectionWithNotes>> {
        return flow {
            val sections: List<Section> = sectionRepository.getSections().singleOrNull() ?: emptyList()
            sections.map {
                val notes = noteRepository.getNotes(it.typeId).singleOrNull() ?: emptyList()
                SectionWithNotes(it, notes)
            }
        }
    }
}