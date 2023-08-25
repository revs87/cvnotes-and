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

    @Query("SELECT * FROM section WHERE id = :id")
    fun getSectionById(id: Int): Section?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSection(section: Section)

    @Delete
    fun deleteSection(section: Section)
}