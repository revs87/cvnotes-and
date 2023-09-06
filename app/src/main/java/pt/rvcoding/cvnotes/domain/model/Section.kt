package pt.rvcoding.cvnotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.rvcoding.cvnotes.domain.util.SectionType
import pt.rvcoding.cvnotes.theme.Blue200
import pt.rvcoding.cvnotes.theme.Blue300
import pt.rvcoding.cvnotes.theme.Blue500
import pt.rvcoding.cvnotes.theme.Gray300
import pt.rvcoding.cvnotes.theme.Green300
import pt.rvcoding.cvnotes.theme.Green500

@Entity
data class Section(
    val typeId: Int,
    var description: String = "",
    val colorId: Int = 0,
    val isSelected: Boolean = false,
    var userId: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int? = null
) {
    companion object {
        val Default by lazy { Section(userId = "", typeId = SectionType.ALL.typeId) }
        val Sections by lazy { SectionType.values().toList().filter { it != SectionType.ALL && it != SectionType.NONE} }
        val Colors by lazy { listOf(Blue200, Blue300, Blue500, Green300, Green500, Gray300) }
    }
}
