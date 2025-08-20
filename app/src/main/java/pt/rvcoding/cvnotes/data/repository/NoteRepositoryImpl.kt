package pt.rvcoding.cvnotes.data.repository

import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.data.source.NoteDao
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getNotes(sectionId: Int): Flow<List<Note>> {
        return dao.getNotes(sectionId)
    }

    override fun getNotesList(sectionId: Int): List<Note> {
        return dao.getNotesList(sectionId)
    }

    override suspend fun getNoteById(id: Long): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    override fun hasSelectedNotes(sectionId: Int): Boolean {
        return dao.hasSelectedNotes(sectionId)
    }

    override suspend fun unselectAllNotes(sectionId: Int) {
        dao.unselectAllNotes(sectionId)
    }

    override suspend fun deleteSelectedNotes(sectionId: Int) {
        dao.deleteSelectedNotes(sectionId)
    }
}