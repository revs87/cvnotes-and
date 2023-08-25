package pt.android.cvnotes.domain.use_case

import pt.android.cvnotes.domain.use_case.section.DeleteSection
import pt.android.cvnotes.domain.use_case.section.GetSectionById
import pt.android.cvnotes.domain.use_case.section.GetSections
import pt.android.cvnotes.domain.use_case.section.GetSectionsWithNotes
import pt.android.cvnotes.domain.use_case.section.InsertSection

data class SectionUseCases(
    val getSectionsWithNotes: GetSectionsWithNotes,
    val getSections: GetSections,
    val getSectionById: GetSectionById,
    val insertSection: InsertSection,
    val deleteSection: DeleteSection
)