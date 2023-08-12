package pt.android.cvnotes.domain.util


enum class NoteType(val id: Int, val typeName: String) {
    ALL(0, ""),
    TEXT(1, "Text"),
    KEY_VALUE_COLON_SEPARATED(2, "Key-value pair colon separated"),
    BULLET(3, "Bullet"),
    BULLET_2ND_LEVEL(4, "Bullet of 2nd level"),
    TIMEFRAME(5, "Timeframe"),
}

fun Int.toNoteType(): NoteType = when (this) {
    NoteType.TEXT.id -> NoteType.TEXT
    NoteType.KEY_VALUE_COLON_SEPARATED.id -> NoteType.KEY_VALUE_COLON_SEPARATED
    NoteType.BULLET.id -> NoteType.BULLET
    NoteType.BULLET_2ND_LEVEL.id -> NoteType.BULLET_2ND_LEVEL
    NoteType.TIMEFRAME.id -> NoteType.TIMEFRAME
    else -> NoteType.ALL
}