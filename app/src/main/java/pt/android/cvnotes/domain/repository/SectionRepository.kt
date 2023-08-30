package pt.android.cvnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.domain.model.Section

interface SectionRepository {
    fun getSections(): Flow<List<Section>>
    fun getSectionsList(): List<Section>
    suspend fun getSectionById(id: Int): Section?
    suspend fun insertSection(section: Section)
    suspend fun deleteSection(section: Section)
    suspend fun deleteSelectedSections()
    fun hasSelectedSections(): Flow<Boolean>
    suspend fun unselectAllSections()
}