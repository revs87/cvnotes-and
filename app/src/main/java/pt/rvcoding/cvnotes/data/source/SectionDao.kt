package pt.rvcoding.cvnotes.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.domain.model.Section


@Dao
interface SectionDao {
    @Query("SELECT * FROM section WHERE userId = :uid")
    fun getSections(uid: String): Flow<List<Section>>

    @Query("SELECT * FROM section WHERE userId = :uid")
    fun getSectionsList(uid: String): List<Section>

    @Query("SELECT * FROM section WHERE id = :id")
    fun getSectionById(id: Int): Section?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSection(section: Section)

    @Delete
    fun deleteSection(section: Section)

    @Query("DELETE FROM section WHERE userId = :uid AND isSelected = :isSelected")
    fun deleteSelectedSections(uid: String, isSelected: Boolean = true)

    @Query("SELECT EXISTS (SELECT * FROM section WHERE userId = :uid AND isSelected = :isSelected)")
    fun hasSelectedSections(uid: String, isSelected: Boolean = true): Flow<Boolean>

    @Query("UPDATE section SET isSelected = :isNotSelected WHERE userId = :uid AND isSelected = :isSelected")
    fun unselectAllSections(uid: String, isSelected: Boolean = true, isNotSelected: Boolean = false)
}