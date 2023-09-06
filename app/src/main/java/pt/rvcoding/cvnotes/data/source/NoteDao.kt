package pt.rvcoding.cvnotes.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.domain.model.Note


@Dao
interface NoteDao {
    @Query("SELECT * FROM note WHERE sectionId = :sectionId")
    fun getNotes(sectionId: Int): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE sectionId = :sectionId")
    fun getNotesList(sectionId: Int): List<Note>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT EXISTS (SELECT * FROM note WHERE sectionId = :sectionId AND isSelected = :isSelected)")
    fun hasSelectedNotes(sectionId: Int, isSelected: Boolean = true): Flow<Boolean>

    @Query("UPDATE note SET isSelected = :isNotSelected WHERE sectionId = :sectionId AND isSelected = :isSelected")
    fun unselectAllNotes(sectionId: Int, isSelected: Boolean = true, isNotSelected: Boolean = false)

    @Query("DELETE FROM note WHERE sectionId = :sectionId AND isSelected = :isSelected")
    fun deleteSelectedNotes(sectionId: Int, isSelected: Boolean = true)
}