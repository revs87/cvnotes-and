package pt.rvcoding.cvnotes.ui.editnote

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
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.isDoubleContent
import pt.rvcoding.cvnotes.domain.use_case.NoteUseCases
import pt.rvcoding.cvnotes.domain.use_case.SectionUseCases
import pt.rvcoding.cvnotes.domain.util.NoteType
import pt.rvcoding.cvnotes.ui.util.L
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val sectionUseCases: SectionUseCases,
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(EditNoteState())
    val state: State<EditNoteState> = _state


    fun getNote(sectionId: Int, noteId: Long) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            val section = sectionUseCases.getSectionById(sectionId)
            val note = noteUseCases.getNoteById(noteId)

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    section = section,
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
        viewModelScope.launch(Dispatchers.Default) {
            val section = sectionUseCases.getSectionById(sectionId)
            _state.value = _state.value.copy(
                section = section,
                note = Note(sectionId = sectionId),
                isLoading = false
            )
        }
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

    fun getNoteDateTime(note: Note?) = when (note) {
        null -> ""
        else -> note.timestamp.toString()
    }
}