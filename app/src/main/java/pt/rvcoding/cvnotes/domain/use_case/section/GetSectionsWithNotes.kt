package pt.rvcoding.cvnotes.domain.use_case.section

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.model.SectionWithNotes
import pt.rvcoding.cvnotes.domain.repository.NoteRepository
import pt.rvcoding.cvnotes.domain.repository.SectionRepository
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import pt.rvcoding.cvnotes.domain.util.sha256

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