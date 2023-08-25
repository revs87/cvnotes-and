package pt.android.cvnotes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.android.cvnotes.domain.model.Section


@Database(
    entities = [Section::class],
    version = 1
)
abstract class SectionDatabase : RoomDatabase() {
    abstract val sectionDao: SectionDao

    companion object {
        const val DATABASE_NAME = "sections_db"
    }
}