package pt.android.cvnotes.ui.editnote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.isDoubleContent
import pt.android.cvnotes.domain.use_case.NoteUseCases
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.ui.util.L
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(EditNoteState())
    val state: State<EditNoteState> = _state


    fun getNote(noteId: Long) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            val note = noteUseCases.getNoteById(noteId)

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    note = note,
                    isLoading = false
                )
            }
        }

    }

    private var addNoteJob: Job? = null
    fun addNote(note: Note) {
        L.i("EditNoteViewModel", "Adding new Note to sectionId: ${note.sectionId}, ${viewModelScope.isActive}")
        addNoteJob?.let { if (it.isActive) { it.cancel() } }
        addNoteJob = viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(isLoading = true)
            }
            noteUseCases.insertNote(note)
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    note = note,
                    isLoading = false
                )
            }
        }
    }

    fun setSectionId(sectionId: Int) {
        _state.value = _state.value.copy(
            note = Note(sectionId = sectionId),
            isLoading = false
        )
    }

    fun saveStatePartialNote(note: Note): Note {
        _state.value = _state.value.copy(
            note = note,
            isLoading = false
        )
        return note
    }

    fun isValid(note: Note?): Boolean =
        when {
            note == null -> false
            note.type == NoteType.NONE.id -> false
            note.content1.isBlank() -> false
            note.content2.isBlank() && note.isDoubleContent() -> false
            else -> true
        }

    fun updateStateNode(note: Note?, newContent: String, isContent1: Boolean = true) {
        note ?: return
        _state.value = _state.value.copy(
            note = note.copy(
                content1 = if (isContent1) { newContent } else { note.content1 },
                content2 = if (!isContent1) { newContent } else { note.content2 },
                timestamp = Date().time
            )
        )
    }
}