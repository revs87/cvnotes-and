package pt.android.cvnotes.domain.util

import pt.android.cvnotes.domain.model.bullet


enum class NoteType(val id: Int, val typeName: String, val example: String) {
    NONE(0, "-", "-"),
    TEXT(1, "Text", "<TEXT>"),
    BULLET(2, "Bullet", "<$bullet TEXT>"),
    BULLET_2ND_LEVEL(3, "2nd level Bullet", "<   $bullet TEXT>"),
    KEY_VALUE_COLON_SEPARATED(4, "Key-value pair colon separated", "<LABEL: TEXT>"),
    KEY_VALUE_COLON_SEPARATED_WITH_BULLET(5, "Key-value pair with bullet", "<$bullet LABEL: TEXT>"),
    KEY_VALUE_COLON_SEPARATED_WITH_BULLET_2ND_LEVEL(6, "Key-value pair with 2nd level bullet", "<    $bullet LABEL: TEXT>"),
    TIMEFRAME(7, "Timeframe", "<START_DATE - END_DATE>"),
}

fun Int.toNoteType(): NoteType = when (this) {
    NoteType.TEXT.id -> NoteType.TEXT
    NoteType.BULLET.id -> NoteType.BULLET
    NoteType.BULLET_2ND_LEVEL.id -> NoteType.BULLET_2ND_LEVEL
    NoteType.KEY_VALUE_COLON_SEPARATED.id -> NoteType.KEY_VALUE_COLON_SEPARATED
    NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id -> NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET
    NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET_2ND_LEVEL.id -> NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET_2ND_LEVEL
    NoteType.TIMEFRAME.id -> NoteType.TIMEFRAME
    else -> NoteType.NONE
}