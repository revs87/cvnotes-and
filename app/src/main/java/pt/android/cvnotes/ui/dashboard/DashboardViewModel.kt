package pt.android.cvnotes.ui.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pt.android.cvnotes.domain.use_case.NoteUseCases
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
): ViewModel() {

    private val _state = mutableStateOf(DashboardState())
    val state: State<DashboardState> = _state

    fun getAllNotes() {
        _state.value = _state.value.copy(isLoading = true)
        _state.value = _state.value.copy(
            notes = noteUseCases.getNotes(),
            isLoading = false
        )
    }

}