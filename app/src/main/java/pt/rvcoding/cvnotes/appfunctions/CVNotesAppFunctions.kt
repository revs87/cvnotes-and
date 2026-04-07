package pt.rvcoding.cvnotes.appfunctions

import android.content.Context
import android.content.Intent
import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.random.Random
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.use_case.NoteUseCases
import pt.rvcoding.cvnotes.domain.use_case.SectionUseCases
import pt.rvcoding.cvnotes.domain.util.NoteType
import pt.rvcoding.cvnotes.domain.util.SectionType
import pt.rvcoding.cvnotes.ui.MainActivity

/**
 * Exposes CVNotes actions to the Android AppFunctions pipeline (Android 16+).
 * See [AppFunctions overview](https://developer.android.com/ai/appfunctions).
 */
@Singleton
class CVNotesAppFunctions @Inject constructor(
    private val sectionUseCases: SectionUseCases,
    private val noteUseCases: NoteUseCases,
    private val pendingNavigationHolder: PendingNavigationHolder,
    @ApplicationContext private val appContext: Context,
) {

    private fun bringAppToForeground() {
        appContext.startActivity(
            Intent(appContext, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        )
    }

    /**
     * Opens the CVNotes main screen on the Dashboard tab.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun navigateToDashboard(appFunctionContext: AppFunctionContext): AppFunctionOperationResult {
        pendingNavigationHolder.setPending(PendingNav.Dashboard)
        bringAppToForeground()
        return AppFunctionOperationResult(true, "Opening dashboard.")
    }

    /**
     * Opens a section’s detail screen.
     *
     * @param sectionId Database id of the section (see [listSections]).
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun navigateToSectionDetails(
        appFunctionContext: AppFunctionContext,
        sectionId: Int,
    ): AppFunctionOperationResult {
        pendingNavigationHolder.setPending(PendingNav.SectionDetails(sectionId))
        bringAppToForeground()
        return AppFunctionOperationResult(true, "Opening section $sectionId.")
    }

    /**
     * Opens the “new note” editor for a section.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun navigateToNewNote(
        appFunctionContext: AppFunctionContext,
        sectionId: Int,
    ): AppFunctionOperationResult {
        pendingNavigationHolder.setPending(PendingNav.NewNote(sectionId))
        bringAppToForeground()
        return AppFunctionOperationResult(true, "Opening new note for section $sectionId.")
    }

    /**
     * Opens the editor for an existing note.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun navigateToEditNote(
        appFunctionContext: AppFunctionContext,
        sectionId: Int,
        noteId: Long,
    ): AppFunctionOperationResult {
        pendingNavigationHolder.setPending(PendingNav.EditNote(sectionId, noteId))
        bringAppToForeground()
        return AppFunctionOperationResult(true, "Opening note $noteId.")
    }

    /**
     * Creates a new section. [sectionTypeId] defaults to Other; use values from [SectionType] (e.g. 7 = Other).
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun addSection(
        appFunctionContext: AppFunctionContext,
        sectionName: String,
        sectionTypeId: Int = SectionType.OTHER.typeId,
    ): AppFunctionOperationResult = withContext(Dispatchers.IO) {
        val name = sectionName.trim()
        if (name.isEmpty()) {
            return@withContext AppFunctionOperationResult(false, "Section name must not be empty.")
        }
        val type = SectionType.entries.find { it.typeId == sectionTypeId }
            ?: return@withContext AppFunctionOperationResult(false, "Unknown section type id: $sectionTypeId")
        if (type == SectionType.NONE || type == SectionType.ALL || type == SectionType.TEMPLATE) {
            return@withContext AppFunctionOperationResult(false, "Cannot create section of type ${type.name}")
        }
        sectionUseCases.insertSection(
            Section(
                typeId = type.typeId,
                description = name,
                colorId = Random.nextInt(from = 0, until = Section.Colors.size),
            )
        )
        AppFunctionOperationResult(true, "Added section \"$name\".")
    }

    /**
     * Adds a plain text note to a section.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun addTextNote(
        appFunctionContext: AppFunctionContext,
        sectionId: Int,
        content: String,
    ): AppFunctionOperationResult = withContext(Dispatchers.IO) {
        val text = content.trim()
        if (text.isEmpty()) {
            return@withContext AppFunctionOperationResult(false, "Note content must not be empty.")
        }
        val section = sectionUseCases.getSectionById(sectionId)
            ?: return@withContext AppFunctionOperationResult(false, "Section $sectionId not found.")
        noteUseCases.insertNote(
            Note(
                sectionId = section.id ?: sectionId,
                type = NoteType.TEXT.id,
                content1 = text,
                content2 = "",
                isSelected = false,
            )
        )
        AppFunctionOperationResult(true, "Note added to section \"${section.description}\".")
    }

    /**
     * Updates a section title/description.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun updateSectionName(
        appFunctionContext: AppFunctionContext,
        sectionId: Int,
        newName: String,
    ): AppFunctionOperationResult = withContext(Dispatchers.IO) {
        val name = newName.trim()
        if (name.isEmpty()) {
            return@withContext AppFunctionOperationResult(false, "New name must not be empty.")
        }
        val section = sectionUseCases.getSectionById(sectionId)
            ?: return@withContext AppFunctionOperationResult(false, "Section $sectionId not found.")
        sectionUseCases.insertSection(section.apply { description = name })
        AppFunctionOperationResult(true, "Section renamed to \"$name\".")
    }

    /**
     * Updates a note’s primary and/or secondary content. Omitted parameters keep existing values.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun updateNoteContents(
        appFunctionContext: AppFunctionContext,
        noteId: Long,
        content1: String? = null,
        content2: String? = null,
    ): AppFunctionOperationResult = withContext(Dispatchers.IO) {
        if (content1 == null && content2 == null) {
            return@withContext AppFunctionOperationResult(false, "Provide content1 and/or content2.")
        }
        val existing = noteUseCases.getNoteById(noteId)
            ?: return@withContext AppFunctionOperationResult(false, "Note $noteId not found.")
        val updated = existing.copy(
            content1 = content1 ?: existing.content1,
            content2 = content2 ?: existing.content2,
        )
        noteUseCases.insertNote(updated)
        AppFunctionOperationResult(true, "Note $noteId updated.")
    }

    /**
     * Lists sections so agents can read ids for navigation and edits.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun listSections(appFunctionContext: AppFunctionContext): List<AppFunctionSectionSummary>? =
        withContext(Dispatchers.IO) {
            val sections = sectionUseCases.getSections().first()
            val list = sections.mapNotNull { s ->
                val id = s.id ?: return@mapNotNull null
                AppFunctionSectionSummary(
                    id = id,
                    title = s.description,
                    typeId = s.typeId,
                )
            }
            list.takeIf { it.isNotEmpty() }
        }

    /**
     * Lists notes in a section (ids for [navigateToEditNote] / [updateNoteContents]).
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun listNotesForSection(
        appFunctionContext: AppFunctionContext,
        sectionId: Int,
    ): List<AppFunctionNoteSummary>? = withContext(Dispatchers.IO) {
        val notes = noteUseCases.getNotesBySectionId(sectionId)
        val list = notes.mapNotNull { n ->
            val id = n.id ?: return@mapNotNull null
            AppFunctionNoteSummary(
                id = id,
                sectionId = n.sectionId,
                preview = n.content1.take(120),
            )
        }
        list.takeIf { it.isNotEmpty() }
    }
}
