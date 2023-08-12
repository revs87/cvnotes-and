package pt.android.cvnotes.data.repository

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.data.source.NoteDao
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}