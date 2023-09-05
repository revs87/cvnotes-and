package pt.android.cvnotes.domain.use_case

import pt.android.cvnotes.domain.use_case.note.DeleteNote
import pt.android.cvnotes.domain.use_case.note.DeleteSelectedNotes
import pt.android.cvnotes.domain.use_case.note.GetNoteById
import pt.android.cvnotes.domain.use_case.note.GetNotes
import pt.android.cvnotes.domain.use_case.note.GetNotesBySectionId
import pt.android.cvnotes.domain.use_case.note.HasSelectedNotes
import pt.android.cvnotes.domain.use_case.note.InsertNote
import pt.android.cvnotes.domain.use_case.note.UnselectAllNotes

data class NoteUseCases(
    val getNotes: GetNotes,
    val getNotesBySectionId: GetNotesBySectionId,
    val getNoteById: GetNoteById,
    val insertNote: InsertNote,
    val deleteNote: DeleteNote,
    val hasSelectedNotes: HasSelectedNotes,
    val unselectAllNotes: UnselectAllNotes,
    val deleteSelectedNotes: DeleteSelectedNotes,
)