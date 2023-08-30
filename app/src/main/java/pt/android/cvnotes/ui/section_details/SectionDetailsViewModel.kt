package pt.android.cvnotes.ui.section_details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
import javax.inject.Inject

@HiltViewModel
class SectionDetailsViewModel @Inject constructor(
    private val sectionUseCases: SectionUseCases,
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(SectionDetailsState())
    val state: State<SectionDetailsState> = _state

    private var job: Job? = null

    fun getSection(sectionId: Int, composeCoroutineScope: CoroutineScope) {
        Log.i("SectionDetailsViewModel", "Entered sectionId: $sectionId, ${viewModelScope.isActive}, ${composeCoroutineScope.isActive}")
        job?.let { if (it.isActive) { it.cancel() } }
        job = composeCoroutineScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(isLoading = true)
            }
            val section = async { sectionUseCases.getSectionById(sectionId) }.await()
            val notes = async { noteUseCases.getNotesBySectionId(sectionId) }.await()
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    section = section ?: Section.Default,
                    notes = notes,
                    isLoading = false,
                    errorMessage = section?.let { "" } ?: "Section not found"
                )
            }
        }
    }

    fun addNote(note: Note, lifecycleScope: LifecycleCoroutineScope) {
        Log.i("SectionDetailsViewModel", "Adding new Note to sectionId: ${note.sectionId}, ${viewModelScope.isActive}, ${lifecycleScope.isActive}")
        job?.let { if (it.isActive) { it.cancel() } }
        job = lifecycleScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(isLoading = true)
            }
            noteUseCases.insertNote(note)
            val section = async { sectionUseCases.getSectionById(note.sectionId) }.await()
            val notes = async { noteUseCases.getNotesBySectionId(note.sectionId) }.await()
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    section = section ?: Section.Default,
                    notes = notes,
                    isLoading = false,
                    errorMessage = section?.let { "" } ?: "Section not found"
                )
            }
        }
    }

}