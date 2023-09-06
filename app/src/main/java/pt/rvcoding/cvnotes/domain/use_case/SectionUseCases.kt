package pt.rvcoding.cvnotes.domain.use_case

import pt.rvcoding.cvnotes.domain.use_case.section.DeleteSection
import pt.rvcoding.cvnotes.domain.use_case.section.DeleteSelectedSections
import pt.rvcoding.cvnotes.domain.use_case.section.GetSectionById
import pt.rvcoding.cvnotes.domain.use_case.section.GetSections
import pt.rvcoding.cvnotes.domain.use_case.section.GetSectionsWithNotes
import pt.rvcoding.cvnotes.domain.use_case.section.HasSelectedSections
import pt.rvcoding.cvnotes.domain.use_case.section.InsertSection
import pt.rvcoding.cvnotes.domain.use_case.section.SelectSection
import pt.rvcoding.cvnotes.domain.use_case.section.UnselectAllSections

data class SectionUseCases(
    val getSectionsWithNotes: GetSectionsWithNotes,
    val getSections: GetSections,
    val getSectionById: GetSectionById,
    val insertSection: InsertSection,
    val selectSection: SelectSection,
    val deleteSection: DeleteSection,
    val deleteSelectedSections: DeleteSelectedSections,
    val hasSelectedSections: HasSelectedSections,
    val unselectAllSections: UnselectAllSections,
)