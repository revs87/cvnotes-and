package pt.android.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.repository.SectionRepository

class GetSections(
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(): Flow<List<Section>> {
        return sectionRepository.getSections()
    }
}