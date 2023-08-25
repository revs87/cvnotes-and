package pt.android.cvnotes.domain.use_case.section

import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.repository.SectionRepository

class DeleteSection(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(section: Section) {
        sectionRepository.deleteSection(section)
    }
}