package pt.android.cvnotes.domain.use_case.note

import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.repository.SectionRepository

class GetSectionById(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(id: Long): Section? {
        return sectionRepository.getSectionById(id)
    }
}