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
import kotlin.random.Random

@Entity
data class Section(
    val typeId: Int,
    val description: String = "",
    val colorId: Int = 0,
    val isSelected: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
) {
    companion object {
        val default by lazy { Section(typeId = SectionType.ALL.id) }
        val Sections by lazy { SectionType.values().toList().filter { it != SectionType.ALL && it != SectionType.NONE} }
        val Colors by lazy { listOf(Blue200, Blue300, Blue500, Green300, Green500, Gray300) }
    }
}
