package pt.android.cvnotes.ui.section_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.use_case.NoteUseCases
import pt.android.cvnotes.domain.use_case.SectionUseCases
import pt.android.cvnotes.ui.util.L
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
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(isLoading = true)
            }
            val section = async { sectionUseCases.getSectionById(sectionId) }.await()
            val notes = async { noteUseCases.getNotesBySectionId(sectionId) }.await()
            val hasSelectedNote = async { noteUseCases.hasSelectedNotes(sectionId) }.await()
            withContext(Dispatchers.Main) {
                _sectionNameEditState.value = section?.description ?: ""
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

    fun updateSectionNewNameState(newName: String) {
        _sectionNameEditState.value = newName
    }

    fun updateSection(sectionId: Int, newName: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val section = sectionUseCases.getSectionById(sectionId)
            section ?: return@launch
            sectionUseCases.insertSection(section.apply { description = newName.trim() })
            val newSection = async { sectionUseCases.getSectionById(sectionId) }.await()
            val notes = async { noteUseCases.getNotesBySectionId(sectionId) }.await()
            val hasSelectedNote = async { noteUseCases.hasSelectedNotes(sectionId) }.await()
            withContext(Dispatchers.Main) {
                _sectionNameEditState.value = section.description
                _state.value = _state.value.copy(
                    section = newSection ?: Section.Default,
                    notes = notes,
                    hasSelectedNotes = hasSelectedNote,
                    isLoading = false,
                    errorMessage = ""
                )
            }
        }
    }

    private var toggleNoteJob: Job? = null
    fun toggleNoteSelection(note: Note) {
        toggleNoteJob?.let { if (it.isActive) { it.cancel() } }
        toggleNoteJob = viewModelScope.launch(Dispatchers.Default) {
            noteUseCases.insertNote(note.apply { isSelected = !isSelected })
            val section = async { sectionUseCases.getSectionById(note.sectionId) }.await()
            val notes = async { noteUseCases.getNotesBySectionId(note.sectionId) }.await()
            val hasSelectedNote = async { noteUseCases.hasSelectedNotes(note.sectionId) }.await()
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
}