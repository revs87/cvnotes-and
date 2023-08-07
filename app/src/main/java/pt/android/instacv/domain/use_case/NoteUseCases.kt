package pt.android.instacv.domain.use_case

import pt.android.instacv.domain.use_case.note.DeleteNote
import pt.android.instacv.domain.use_case.note.GetNoteById
import pt.android.instacv.domain.use_case.note.GetNotes
import pt.android.instacv.domain.use_case.note.InsertNote

data class NoteUseCases(
    val getNotes: GetNotes,
    val getNoteById: GetNoteById,
    val insertNote: InsertNote,
    val deleteNote: DeleteNote
)