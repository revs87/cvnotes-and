package pt.rvcoding.cvnotes.domain.use_case.section

import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.repository.SectionRepository

class GetSectionById(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(id: Int): Section? {
        return sectionRepository.getSectionById(id)
    }
}