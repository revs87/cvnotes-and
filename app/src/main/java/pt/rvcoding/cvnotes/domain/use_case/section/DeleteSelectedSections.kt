package pt.rvcoding.cvnotes.domain.use_case.section

import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.repository.SectionRepository
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository

class DeleteSelectedSections(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke() {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        sectionRepository.deleteSelectedSections(uid)
    }
}