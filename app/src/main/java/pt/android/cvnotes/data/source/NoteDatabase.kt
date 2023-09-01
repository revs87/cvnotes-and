package pt.android.cvnotes.data.source

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import pt.android.cvnotes.domain.model.Note


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
                Log.i("NoteDatabase", "Migrating database: MIGRATION_1_2")
                database.execSQL("ALTER TABLE note ADD COLUMN isSelected2 INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}