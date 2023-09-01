package pt.android.cvnotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.domain.util.toNoteType
import java.util.Date

@Entity
data class Note(
    val sectionId: Int,
    val type: Int = NoteType.NONE.id,
    val content1: String = "",
    val content2: String = "",
    val timestamp: Long = Date().time,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    companion object {
        val default by lazy { Note(sectionId = 0, type = NoteType.NONE.id) }
        val types by lazy { NoteType.values().toList().filter { it != NoteType.NONE } }
    }
}

fun Note.shouldHaveContent2(): Boolean =
    this.type == NoteType.KEY_VALUE_COLON_SEPARATED.id
            && this.type == NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id
            && this.type == NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET_2ND_LEVEL.id
            && this.type == NoteType.TIMEFRAME.id


// • = \u2022,   ● = \u25CF,   ○ = \u25CB,   ▪ = \u25AA,   ■ = \u25A0,   □ = \u25A1,   ► = \u25BA
fun Note.asString(): String =
    when (this.type.toNoteType()) {
        NoteType.NONE -> ""
        NoteType.TEXT -> this.content1
        NoteType.BULLET -> "\u2022 ${this.content1}"
        NoteType.BULLET_2ND_LEVEL -> "\t \u2022 ${this.content1}"
        NoteType.KEY_VALUE_COLON_SEPARATED -> "${this.content1}: ${this.content2}"
        NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET -> "\u2022 ${this.content1}: ${this.content2}"
        NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET_2ND_LEVEL -> "\t \u2022 ${this.content1}: ${this.content2}"
        NoteType.TIMEFRAME -> "${this.content1} - ${this.content2}"
    }