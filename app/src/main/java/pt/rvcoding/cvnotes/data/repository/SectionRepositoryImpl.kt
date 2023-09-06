package pt.rvcoding.cvnotes.data.repository

import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.data.source.SectionDao
import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.repository.SectionRepository

class SectionRepositoryImpl(
    private val dao: SectionDao
) : SectionRepository {
    override fun getSections(uid: String): Flow<List<Section>> {
        return dao.getSections(uid)
    }

    override fun getSectionsList(uid: String): List<Section> {
        return dao.getSectionsList(uid)
    }

    override suspend fun getSectionById(id: Int): Section? {
        return dao.getSectionById(id)
    }

    override suspend fun insertSection(section: Section) {
        dao.insertSection(section)
    }

    override suspend fun deleteSection(section: Section) {
        dao.deleteSection(section)
    }

    override suspend fun deleteSelectedSections(uid: String) {
        dao.deleteSelectedSections(uid)
    }

    override fun hasSelectedSections(uid: String): Flow<Boolean> {
        return dao.hasSelectedSections(uid)
    }

    override suspend fun unselectAllSections(uid: String) {
        dao.unselectAllSections(uid)
    }
}