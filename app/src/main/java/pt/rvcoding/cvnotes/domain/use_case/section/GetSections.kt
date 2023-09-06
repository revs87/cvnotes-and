package pt.rvcoding.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.repository.SectionRepository
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository

class GetSections(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(): Flow<List<Section>> {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        return sectionRepository.getSections(uid)
    }
}