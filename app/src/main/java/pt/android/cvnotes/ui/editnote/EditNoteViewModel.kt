package pt.android.cvnotes.ui.editnote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import pt.android.cvnotes.domain.use_case.NoteUseCases
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(EditNoteState())
    val state: State<EditNoteState> = _state


    fun getNote(noteId: Long) {
        _state.value = _state.value.copy(isLoading = true)
        _state.value = _state.value.copy(
            note = flow { noteUseCases.getNoteById(noteId) },
            isLoading = false
        )
    }


}