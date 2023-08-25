package pt.android.cvnotes.domain.use_case

import pt.android.cvnotes.domain.use_case.note.DeleteSection
import pt.android.cvnotes.domain.use_case.note.GetSectionById
import pt.android.cvnotes.domain.use_case.note.GetSections
import pt.android.cvnotes.domain.use_case.note.InsertSection

data class SectionUseCases(
    val getSections: GetSections,
    val getSectionById: GetSectionById,
    val insertSection: InsertSection,
    val deleteSection: DeleteSection
)