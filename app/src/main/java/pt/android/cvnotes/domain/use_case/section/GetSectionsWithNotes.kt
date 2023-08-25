package pt.android.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.SectionWithNotes
import pt.android.cvnotes.domain.repository.NoteRepository
import pt.android.cvnotes.domain.repository.SectionRepository

class GetSectionsWithNotes(
    private val sectionRepository: SectionRepository,
    private val noteRepository: NoteRepository,
) {
    operator fun invoke(): Flow<List<SectionWithNotes>> = flow {
        sectionRepository.getSections().map { sections ->
            sections.map { section -> SectionWithNotes(section, getNotes(section.typeId)) }
        }.collect { emit(it) }
    }

    private fun getNotes(typeId: Int): List<Note> {
        val notes: MutableList<Note> = mutableListOf()
        noteRepository.getNotes(typeId).onEach { notes.addAll(it) }
        return notes
    }
}