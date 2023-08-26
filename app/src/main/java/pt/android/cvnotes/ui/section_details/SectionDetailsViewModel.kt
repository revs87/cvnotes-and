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
    fun getSection(sectionId: Int) {
        println("sectionId: $sectionId active?${viewModelScope.isActive}")

        job?.let { if(it.isActive) { it.cancel() } }
        job = viewModelScope.launch(Dispatchers.Default) {
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

//        println("sectionId: $sectionId")
//        val flow = flow {
//            val section = sectionUseCases.getSectionById(sectionId)
//            val notes = noteUseCases.getNotesBySectionId(sectionId)
//            emit(
//                SectionDetailsState(
//                    section = section ?: Section.Default,
//                    notes = notes,
//                    isLoading = false,
//                    errorMessage = section?.let { "" } ?: "Section not found"
//                )
//            )
//        }.onStart {
//            emit(SectionDetailsState(isLoading = true))
//        }.onCompletion {
//            println("sectionId: $sectionId")
//        }.onEach {
//            println("sectionId-isLoading: ${it.isLoading}")
//        }
//
//        job?.cancel()
//        job = viewModelScope.launch(Dispatchers.Default) {
//            val last = flow.last()
//            withContext(Dispatchers.Main) {
//                println("sectionId-last: ${last.isLoading}")
//                _state.value = last
//            }
//        }

    }


}