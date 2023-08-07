package pt.android.instacv.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.android.instacv.domain.util.NoteSection
import pt.android.instacv.domain.util.NoteType
import pt.android.instacv.theme.Blue200
import pt.android.instacv.theme.Blue300
import pt.android.instacv.theme.Blue500
import pt.android.instacv.theme.Gray300
import pt.android.instacv.theme.Green300
import pt.android.instacv.theme.Green500

@Entity
data class Note(
    val section: Int,
    val type: Int,
    val content1: String,
    val content2: String = "",
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val sections by lazy { NoteSection.values().toList().filter { it != NoteSection.ALL } }
        val types by lazy { NoteType.values().toList().filter { it != NoteType.ALL } }
        val colors by lazy { listOf(Blue200, Blue300, Blue500, Green300, Green500, Gray300) }
    }
}
