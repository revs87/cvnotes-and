package pt.android.instacv.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pt.android.instacv.data.Result
import pt.android.instacv.data.remote.firebase.AuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataRepository: AuthenticationRepository
) : ViewModel() {

    private val _fieldsState = mutableStateOf(AuthFieldsState())
    val fieldsState: State<AuthFieldsState> = _fieldsState
    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state


    fun createUser(email: String, pwd: String) {
        _state.value = AuthState(isLoading = true)
        dataRepository.register(email, pwd)
            .onEach { res ->
                when (res) {
                    is Result.Error -> _state.value = AuthState(errorMessage = res.message)
                    is Result.Success -> _state.value = AuthState(isLoggedIn = true)
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateEmail(emailChange: String) {
        _fieldsState.value = AuthFieldsState(emailChange, _fieldsState.value.pwdValue)
    }

    fun updatePwd(pwdChange: String) {
        _fieldsState.value = AuthFieldsState(_fieldsState.value.emailValue, pwdChange)
    }

}