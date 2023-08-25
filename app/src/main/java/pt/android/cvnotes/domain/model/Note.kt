package pt.android.cvnotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.android.cvnotes.domain.util.NoteType

@Entity
data class Note(
    val sectionId: Int,
    val type: Int,
    val content1: String = "",
    val content2: String = "",
    val timestamp: Long = 0L,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    companion object {
        val default by lazy { Note(sectionId = 0, type = NoteType.ALL.id) }
        val types by lazy { NoteType.values().toList().filter { it != NoteType.ALL } }
    }
}
