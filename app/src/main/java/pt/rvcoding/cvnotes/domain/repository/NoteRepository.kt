package pt.rvcoding.cvnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.domain.model.Note

interface NoteRepository {
    fun getNotes(sectionId: Int): Flow<List<Note>>
    fun getNotesList(sectionId: Int): List<Note>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun hasSelectedNotes(sectionId: Int): Boolean
    suspend fun unselectAllNotes(sectionId: Int)
    suspend fun deleteSelectedNotes(sectionId: Int)
}