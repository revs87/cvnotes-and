package pt.android.instacv.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pt.android.instacv.data.Result
import pt.android.instacv.data.local.SPKey
import pt.android.instacv.data.local.SharedPreferencesRepository
import pt.android.instacv.data.remote.firebase.AuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataRepository: AuthenticationRepository,
    private val spRepository: SharedPreferencesRepository,
) : ViewModel() {

    private val _fieldsState = mutableStateOf(AuthFieldsState())
    val fieldsState: State<AuthFieldsState> = _fieldsState
    private val _state = mutableStateOf(AuthState(isLoggedIn = isLoggedIn()))
    val state: State<AuthState> = _state


    fun createUser(email: String, pwd: String) {
        _state.value = asLoadingActive()
        dataRepository.register(email, pwd)
            .onEach { res ->
                when (res) {
                    is Result.Error -> _state.value = asError(res)
                    is Result.Success -> _state.value = asSuccess()
                }
            }
            .launchIn(viewModelScope)
    }

    fun logUser(email: String, pwd: String) {
        _state.value = asLoadingActive()
        dataRepository.login(email, pwd)
            .onEach { res ->
                when (res) {
                    is Result.Error -> _state.value = asError(res)
                    is Result.Success -> _state.value = asSuccess()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun asSuccess() =
        AuthState(
            section = _state.value.section,
            isLoggedIn = isLoggedIn(),
            isLoading = false,
            errorMessage = _state.value.errorMessage,
        )

    private fun asError(res: Result.Error) =
        AuthState(
            section = _state.value.section,
            isLoggedIn = isLoggedIn(),
            isLoading = false,
            errorMessage = res.message,
        )

    private fun asLoadingActive() =
        AuthState(
            section = _state.value.section,
            isLoggedIn = isLoggedIn(),
            isLoading = true,
            errorMessage = _state.value.errorMessage,
        )
    private fun isLoggedIn(): Boolean = spRepository.getString(SPKey.UID.key).isNotBlank()

    fun updateEmail(emailChange: String) {
        _fieldsState.value = AuthFieldsState(emailChange.trim(), _fieldsState.value.pwdValue)
    }
    fun updatePwd(pwdChange: String) {
        _fieldsState.value = AuthFieldsState(_fieldsState.value.emailValue, pwdChange.trim())
    }
    fun onRegisterClick() {
        _state.value = AuthState(
            section = AuthSection.REGISTER,
            isLoggedIn = _state.value.isLoggedIn,
            isLoading = false,
            errorMessage = _state.value.errorMessage,
        )
    }
    fun onLoginClick() {
        _state.value = AuthState(
            section = AuthSection.LOGIN,
            isLoggedIn = _state.value.isLoggedIn,
            isLoading = false,
            errorMessage = _state.value.errorMessage,
        )
    }
}