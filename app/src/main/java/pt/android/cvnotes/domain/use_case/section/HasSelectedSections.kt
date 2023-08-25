package pt.android.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.domain.repository.SectionRepository

class HasSelectedSections(
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(): Flow<Boolean> = sectionRepository.hasSelectedSections()
}