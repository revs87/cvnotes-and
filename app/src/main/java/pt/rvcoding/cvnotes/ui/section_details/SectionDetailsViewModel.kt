package pt.rvcoding.cvnotes.ui.section_details

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
import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.use_case.NoteUseCases
import pt.rvcoding.cvnotes.domain.use_case.SectionUseCases
import pt.rvcoding.cvnotes.ui.util.L
import javax.inject.Inject

@HiltViewModel
class SectionDetailsViewModel @Inject constructor(
    private val sectionUseCases: SectionUseCases,
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(SectionDetailsState())
    val state: State<SectionDetailsState> = _state
    private val _sectionNameEditState = mutableStateOf("")
    val sectionNameEditState: State<String> = _sectionNameEditState

    private var getSectionJob: Job? = null
    fun getSection(sectionId: Int) {
        L.i("SectionDetailsViewModel", "Entered sectionId: $sectionId, ${viewModelScope.isActive}")
        getSectionJob?.let { if (it.isActive) { it.cancel() } }
        getSectionJob = viewModelScope.launch(Dispatchers.Default) {
            updateUI(sectionId)
        }
    }

    fun updateSectionNewNameState(newName: String) {
        _sectionNameEditState.value = newName
    }

    fun updateSection(sectionId: Int, newName: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val section = sectionUseCases.getSectionById(sectionId)
            section ?: return@launch
            sectionUseCases.insertSection(section.apply { description = newName.trim() })
            updateUI(sectionId)
        }
    }

    private var toggleNoteJob: Job? = null
    fun toggleNoteSelection(note: Note) {
        toggleNoteJob?.let { if (it.isActive) { it.cancel() } }
        toggleNoteJob = viewModelScope.launch(Dispatchers.Default) {
            noteUseCases.insertNote(note.apply { isSelected = !isSelected })
            updateUI(note.sectionId)
        }
    }

    fun unselectAllSelectedNotes(sectionId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            noteUseCases.unselectAllNotes(sectionId)
            updateUI(sectionId)
        }
    }

    fun deleteSelectedNotes(sectionId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            noteUseCases.deleteSelectedNotes(sectionId)
            updateUI(sectionId)
        }
    }

    private suspend fun updateUI(sectionId: Int) {
        withContext(Dispatchers.Main) { _state.value = _state.value.copy(isLoading = true) }
        val section = sectionUseCases.getSectionById(sectionId)
        val notes = noteUseCases.getNotesBySectionId(sectionId)
        val hasSelectedNote = noteUseCases.hasSelectedNotes(sectionId)
        withContext(Dispatchers.Main) {
            _state.value = _state.value.copy(
                section = section ?: Section.Default,
                notes = notes,
                hasSelectedNotes = hasSelectedNote,
                isLoading = false,
                errorMessage = section?.let { "" } ?: "Section not found"
            )
        }
    }
}