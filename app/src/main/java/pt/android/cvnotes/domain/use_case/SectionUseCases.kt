package pt.android.cvnotes.domain.use_case

import pt.android.cvnotes.domain.use_case.section.DeleteSection
import pt.android.cvnotes.domain.use_case.section.DeleteSelectedSections
import pt.android.cvnotes.domain.use_case.section.GetSectionById
import pt.android.cvnotes.domain.use_case.section.GetSections
import pt.android.cvnotes.domain.use_case.section.GetSectionsWithNotes
import pt.android.cvnotes.domain.use_case.section.HasSelectedSections
import pt.android.cvnotes.domain.use_case.section.InsertSection
import pt.android.cvnotes.domain.use_case.section.SelectSection

data class SectionUseCases(
    val getSectionsWithNotes: GetSectionsWithNotes,
    val getSections: GetSections,
    val getSectionById: GetSectionById,
    val insertSection: InsertSection,
    val selectSection: SelectSection,
    val deleteSection: DeleteSection,
    val deleteSelectedSections: DeleteSelectedSections,
    val hasSelectedSections: HasSelectedSections,
)