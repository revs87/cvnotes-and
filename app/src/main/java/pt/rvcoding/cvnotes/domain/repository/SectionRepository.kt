package pt.rvcoding.cvnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.rvcoding.cvnotes.domain.model.Section

interface SectionRepository {
    fun getSections(uid: String): Flow<List<Section>>
    fun getSectionsList(uid: String): List<Section>
    suspend fun getSectionById(id: Int): Section?
    suspend fun insertSection(section: Section)
    suspend fun deleteSection(section: Section)
    suspend fun deleteSelectedSections(uid: String)
    fun hasSelectedSections(uid: String): Flow<Boolean>
    suspend fun unselectAllSections(uid: String)
}