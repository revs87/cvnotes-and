package pt.android.cvnotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.android.cvnotes.domain.util.NoteSection
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.theme.Blue200
import pt.android.cvnotes.theme.Blue300
import pt.android.cvnotes.theme.Blue500
import pt.android.cvnotes.theme.Gray300
import pt.android.cvnotes.theme.Green300
import pt.android.cvnotes.theme.Green500

@Entity
data class Note(
    val section: Int,
    val type: Int,
    val content1: String = "",
    val content2: String = "",
    val timestamp: Long = 0L,
    val color: Int = 0,
    @PrimaryKey val id: Long? = null
) {
    companion object {
        val default by lazy { Note(section = NoteSection.ALL.id, type = NoteType.ALL.id) }
        val sections by lazy { NoteSection.values().toList().filter { it != NoteSection.ALL } }
        val types by lazy { NoteType.values().toList().filter { it != NoteType.ALL } }
        val colors by lazy { listOf(Blue200, Blue300, Blue500, Green300, Green500, Gray300) }
    }
}
