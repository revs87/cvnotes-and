package pt.android.cvnotes.domain.use_case.section

import pt.android.cvnotes.domain.repository.SectionRepository

class SelectSection(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(id: Int) {
        val section = sectionRepository.getSectionById(id)
        val selectedSection = section?.copy(isSelected = !section.isSelected)
        selectedSection?.let { sectionRepository.insertSection(it) }
    }
}