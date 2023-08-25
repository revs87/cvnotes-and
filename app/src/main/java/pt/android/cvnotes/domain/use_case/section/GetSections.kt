package pt.android.cvnotes.domain.use_case.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.repository.SectionRepository
import pt.android.cvnotes.domain.util.SectionType

class GetSections(
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(
        section: SectionType = SectionType.ALL
    ): Flow<List<Section>> {
        return sectionRepository.getSections().map { sections ->
            when (section) {
                SectionType.ALL -> sections
                else -> sections.filter { section.id == it.type }
            }
        }
    }
}