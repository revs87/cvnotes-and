package pt.android.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.data.SPKey
import pt.android.cvnotes.domain.repository.SectionRepository
import pt.android.cvnotes.domain.repository.SharedPreferencesRepository

class HasSelectedSections(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(): Flow<Boolean> {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        return sectionRepository.hasSelectedSections(uid)
    }
}