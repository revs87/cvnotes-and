package pt.android.cvnotes.domain.use_case.section

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import pt.android.cvnotes.data.SPKey
import pt.android.cvnotes.domain.model.SectionWithNotes
import pt.android.cvnotes.domain.repository.NoteRepository
import pt.android.cvnotes.domain.repository.SectionRepository
import pt.android.cvnotes.domain.repository.SharedPreferencesRepository

class GetSectionsWithNotes(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository,
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(scope: CoroutineScope): StateFlow<List<SectionWithNotes>> = flow {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        val sections = sectionRepository.getSectionsList(uid)
        emit(
            sections.map { section ->
                SectionWithNotes(section, noteRepository.getNotesList(section.id ?: 0))
            }
        )
    }.stateIn(scope)
}