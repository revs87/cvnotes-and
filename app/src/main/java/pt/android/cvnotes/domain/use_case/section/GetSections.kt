package pt.android.cvnotes.domain.use_case.section

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.data.SPKey
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.repository.SectionRepository
import pt.android.cvnotes.domain.repository.SharedPreferencesRepository

class GetSections(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(): Flow<List<Section>> {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        return sectionRepository.getSections(uid)
    }
}