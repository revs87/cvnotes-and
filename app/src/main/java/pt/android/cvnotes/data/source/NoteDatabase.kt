package pt.android.cvnotes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.ui.util.L


@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                L.i("NoteDatabase", "Migrating database: MIGRATION_1_2")
                database.execSQL("ALTER TABLE note ADD COLUMN isSelected2 INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}