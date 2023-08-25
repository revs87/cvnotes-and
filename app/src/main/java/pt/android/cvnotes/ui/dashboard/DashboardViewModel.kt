package pt.android.cvnotes.ui.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.use_case.SectionUseCases
import pt.android.cvnotes.domain.util.SectionType
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sectionUseCases: SectionUseCases
): ViewModel() {

    private val _state = mutableStateOf(DashboardState())
    val state: State<DashboardState> = _state

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        _state.value = _state.value.copy(isLoading = true)
        _state.value = _state.value.copy(
            sectionsWithNotes = sectionUseCases.getSectionsWithNotes(),
            isLoading = false
        )
    }

    fun addSection(sectionType: SectionType) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.insertSection(
                Section(sectionType.id, sectionType.sectionName, sectionType.color)
            )
            val list = sectionUseCases.getSectionsWithNotes().toList()
            println(list)
        }
        _state.value = _state.value.copy(
            sectionsWithNotes = sectionUseCases.getSectionsWithNotes(),
            isLoading = false
        )
    }

}