package pt.android.cvnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.android.cvnotes.domain.model.Section

interface SectionRepository {
    fun getSections(): Flow<List<Section>>
    suspend fun getSectionById(id: Long): Section?
    suspend fun insertSection(section: Section)
    suspend fun deleteSection(section: Section)
}