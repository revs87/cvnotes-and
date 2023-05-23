package pt.android.instacv.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pt.android.instacv.data.AuthenticationRepository
import pt.android.instacv.data.Result
import pt.android.instacv.data.local.SharedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: AuthenticationRepository,
    private val spRepository: SharedPreferencesRepository,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun logout() {
        _state.value = asLoadingActive()
        dataRepository.logout()
            .onEach { res ->
                when (res) {
                    is Result.Error -> _state.value = asError(res)
                    is Result.Success -> _state.value = asSuccess()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun asSuccess() =
        HomeState(
            section = HomeSection.AUTH,
            isLoading = false,
            errorMessage = _state.value.errorMessage,
        )

    private fun asError(res: Result.Error) =
        HomeState(
            section = _state.value.section,
            isLoading = false,
            errorMessage = res.message,
        )

    private fun asLoadingActive() =
        HomeState(
            section = _state.value.section,
            isLoading = true,
            errorMessage = _state.value.errorMessage,
        )
}