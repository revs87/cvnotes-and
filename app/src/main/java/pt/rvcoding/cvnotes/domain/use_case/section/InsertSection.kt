package pt.rvcoding.cvnotes.domain.use_case.section

import android.util.Base64
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.repository.SectionRepository
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository

class InsertSection(
    private val spRepository: SharedPreferencesRepository,
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(section: Section) {
        val uid = spRepository.getString(SPKey.EMAIL.key).sha256()
        sectionRepository.insertSection(section.apply { userId = uid })
    }
}

fun String.sha256(): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)