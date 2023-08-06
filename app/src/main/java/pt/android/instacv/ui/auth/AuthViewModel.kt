package pt.android.instacv.ui.auth

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pt.android.instacv.data.Result
import pt.android.instacv.data.local.SPKey
import pt.android.instacv.data.local.SharedPreferencesRepository
import pt.android.instacv.domain.repository.AuthRepository
import pt.android.instacv.ui.util.component.AuthFieldsState
import pt.android.instacv.ui.auth.AuthError.LoginError
import pt.android.instacv.ui.auth.AuthError.RegisterError
import pt.android.instacv.ui.util.L
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val spRepository: SharedPreferencesRepository,
) : ViewModel() {

    private val _fieldsState = mutableStateOf(AuthFieldsState())
    val fieldsState: State<AuthFieldsState> = _fieldsState
    private val _state = mutableStateOf(AuthState(isLoggedIn = isLoggedIn()))
    val state: State<AuthState> = _state
    private val _errors = Channel<String>()
    val errors = _errors.receiveAsFlow()

    private var getRegisterJob: Job? = null
    private var getLoginJob: Job? = null

    fun createUser(email: String, pwd: String) {
        _state.value = asLoadingActive()
        getRegisterJob?.cancel()
        getRegisterJob = authRepository.register(email, pwd)
            .onEach { res ->
                when (res) {
                    is Result.Error -> _state.value = asError(RegisterError(res.message))
                    is Result.Success -> _state.value = asSuccess()
                }
            }
            .launchIn(viewModelScope)
    }

    fun logUser(email: String, pwd: String) {
        _state.value = asLoadingActive()
        getLoginJob?.cancel()
        getLoginJob = authRepository.login(email, pwd)
            .onEach { res ->
                when (res) {
                    is Result.Error -> _state.value = asError(LoginError(res.message))
                    is Result.Success -> _state.value = asSuccess()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun asSuccess() = AuthState(isLoggedIn = isLoggedIn(), isLoading = false)
    private fun asError(error: AuthError): AuthState {
        L.e(TAG, error.message)
        viewModelScope.launch { _errors.send(
            when (error) {
                is LoginError -> error.userMessage
                is RegisterError -> error.userMessage
            }
        )}
        return AuthState(isLoggedIn = isLoggedIn(), isLoading = false)
    }
    private fun asLoadingActive() = AuthState(isLoggedIn = isLoggedIn(), isLoading = true)
    private fun isLoggedIn(): Boolean = spRepository.getString(SPKey.UID.key).isNotBlank()


    fun updateEmail(emailChange: String) {
        _fieldsState.value = AuthFieldsState(emailChange.trim(), _fieldsState.value.pwdValue, emailChange.isValidEmail())
    }
    fun updatePwd(pwdChange: String) {
        _fieldsState.value = AuthFieldsState(_fieldsState.value.emailValue, pwdChange.trim(), _fieldsState.value.submitBtnEnabled)
    }
    fun cleanFields() {
        _fieldsState.value = AuthFieldsState("", "", false)
    }

    private fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()


    companion object {
        const val TAG = "AuthViewModel"
    }
}