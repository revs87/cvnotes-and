package pt.rvcoding.cvnotes.domain.use_case.section

import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.repository.SectionRepository

class DeleteSection(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(section: Section) {
        sectionRepository.deleteSection(section)
    }
}