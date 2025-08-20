package pt.rvcoding.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.repository.SectionRepository
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import pt.rvcoding.cvnotes.domain.util.sha256

class HasSelectedSections(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(): Flow<Boolean> {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        return sectionRepository.hasSelectedSections(uid)
    }
}