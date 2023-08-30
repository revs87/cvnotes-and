package pt.android.cvnotes.ui.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.use_case.NoteUseCases
import pt.android.cvnotes.domain.use_case.SectionUseCases
import pt.android.cvnotes.domain.util.SectionType
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sectionUseCases: SectionUseCases,
    private val noteUseCases: NoteUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(DashboardState())
    val state: State<DashboardState> = _state

    fun getAllNotes() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

    fun addSection(sectionType: SectionType) {
        if (sectionType != SectionType.NONE) {
            _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
            viewModelScope.launch(Dispatchers.Default) {
                sectionUseCases.insertSection(
                    Section(
                        typeId = sectionType.typeId,
                        description = sectionType.sectionName,
                        colorId = Random.nextInt(from = 0, until = Section.Colors.size)
                    )
                )
            }
            viewModelScope.launch(Dispatchers.Default) {
                val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        sectionsWithNotes = sectionWithNotes,
                        sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                        scrollToBottom = true,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectSection(id: Int) {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.selectSection(id)
        }
        viewModelScope.launch(Dispatchers.Default) {
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

    fun unselectAllSelectedSections() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.unselectAllSections()
        }
        viewModelScope.launch(Dispatchers.Default) {
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

    fun deleteSelectedSections() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.deleteSelectedSections()
        }
        viewModelScope.launch(Dispatchers.Default) {
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

}