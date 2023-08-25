package pt.android.cvnotes.domain.use_case.section

import pt.android.cvnotes.domain.repository.SectionRepository

class UnselectAllSections(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke() = sectionRepository.unselectAllSections()
}