package pt.android.cvnotes.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.domain.model.Section


@Dao
interface SectionDao {
    @Query("SELECT * FROM section")
    fun getSections(): Flow<List<Section>>

    @Query("SELECT * FROM section")
    fun getSectionsList(): List<Section>

    @Query("SELECT * FROM section WHERE id = :id")
    fun getSectionById(id: Int): Section?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSection(section: Section)

    @Delete
    fun deleteSection(section: Section)

    @Query("DELETE FROM section WHERE isSelected = :isSelected")
    fun deleteSelectedSections(isSelected: Boolean = true)

    @Query("SELECT EXISTS (SELECT * FROM section WHERE isSelected = :isSelected)")
    fun hasSelectedSections(isSelected: Boolean = true): Flow<Boolean>

    @Query("UPDATE section SET isSelected = :isNotSelected WHERE isSelected = :isSelected")
    fun unselectAllSections(isSelected: Boolean = true, isNotSelected: Boolean = false)
}