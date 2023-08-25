package pt.android.cvnotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.android.cvnotes.domain.util.SectionType
import pt.android.cvnotes.theme.Blue200
import pt.android.cvnotes.theme.Blue300
import pt.android.cvnotes.theme.Blue500
import pt.android.cvnotes.theme.Gray300
import pt.android.cvnotes.theme.Green300
import pt.android.cvnotes.theme.Green500

@Entity
data class Section(
    val type: Int,
    val description: String = "",
    val color: Int = 0,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val default by lazy { Section(type = SectionType.ALL.id) }
        val sections by lazy { SectionType.values().toList().filter { it != SectionType.ALL } }
        val colors by lazy { listOf(Blue200, Blue300, Blue500, Green300, Green500, Gray300) }
    }
}
