package pt.rvcoding.cvnotes.appfunctions

import androidx.appfunctions.AppFunctionSerializable

/**
 * Result of a mutating CVNotes [androidx.appfunctions.service.AppFunction].
 */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class AppFunctionOperationResult(
    /** Whether the operation completed successfully. */
    val success: Boolean,
    /** Human-readable detail or error message. */
    val message: String,
)

/**
 * Summary of a section for agent discovery (ids for navigation and edits).
 */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class AppFunctionSectionSummary(
    val id: Int,
    val title: String,
    val typeId: Int,
)

/**
 * Summary of a note for agent discovery.
 */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class AppFunctionNoteSummary(
    val id: Long,
    val sectionId: Int,
    /** Short preview of primary content. */
    val preview: String,
)
