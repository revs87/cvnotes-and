package pt.android.cvnotes.ui.about

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pt.android.cvnotes.data.Result
import pt.android.cvnotes.data.SPKey
import pt.android.cvnotes.domain.repository.AuthRepository
import pt.android.cvnotes.domain.repository.SharedPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val spRepository: SharedPreferencesRepository
) : ViewModel() {

    private val _state = mutableStateOf(AboutState())
    val state: State<AboutState> = _state
    private val _profileState = mutableStateOf(AboutProfileState())
    val profileState: State<AboutProfileState> = _profileState

    init {
        getProfile()
    }

    private var getLogoutJob: Job? = null
    fun logout() {
        _state.value = asLoadingActive()
        getLogoutJob?.cancel()
        getLogoutJob = authRepository.logout()
            .onEach { res ->
                when (res) {
                    is Result.Error -> _state.value = asError(res)
                    is Result.Success -> _state.value = asSuccess()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getProfile() {
        _profileState.value = AboutProfileState(
            email = spRepository.getString(SPKey.EMAIL.key),
        )
    }

    private fun asSuccess() =
        AboutState(
            section = HomeSection.AUTH,
            isLoading = false,
            errorMessage = _state.value.errorMessage,
        )

    private fun asError(res: Result.Error) =
        AboutState(
            section = _state.value.section,
            isLoading = false,
            errorMessage = res.message,
        )

    private fun asLoadingActive() =
        AboutState(
            section = _state.value.section,
            isLoading = true,
            errorMessage = _state.value.errorMessage,
        )
}