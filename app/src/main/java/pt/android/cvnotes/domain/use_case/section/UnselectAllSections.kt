package pt.android.cvnotes.domain.use_case.section

import pt.android.cvnotes.data.SPKey
import pt.android.cvnotes.domain.repository.SectionRepository
import pt.android.cvnotes.domain.repository.SharedPreferencesRepository

class UnselectAllSections(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke() {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        return sectionRepository.unselectAllSections(uid)
    }
}