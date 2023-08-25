package pt.android.cvnotes.domain.use_case.section

import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.repository.SectionRepository

class GetSectionById(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(id: Int): Section? {
        return sectionRepository.getSectionById(id)
    }
}