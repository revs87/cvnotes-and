package pt.rvcoding.cvnotes.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        _state.value = HomeState()
    }

    fun selectBottomNavPage(index: Int) {
        _state.value = HomeState(selectedBottomItem = index)
    }

}