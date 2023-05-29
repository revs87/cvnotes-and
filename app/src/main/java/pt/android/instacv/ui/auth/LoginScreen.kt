package pt.android.instacv.ui.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import pt.android.instacv.theme.MyTheme
import pt.android.instacv.ui._component.AuthFields
import pt.android.instacv.ui._component.AuthFieldsState
import pt.android.instacv.ui._component.LoadingIndicator

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    state: AuthState = AuthState(),
    fieldsState: AuthFieldsState = AuthFieldsState(),
    updateEmailListener: (newValue: String) -> Unit = {},
    updatePwdListener: (newValue: String) -> Unit = {},
    logUserListener: (email: String, pwd: String) -> Unit = { _, _ -> },
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    MyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { padding ->
                LoadingIndicator(state.isLoading)
                AuthFields(
                    title = "Log into your account.",
                    emailTitle = "Email",
                    pwdTitle = "Password",
                    btnText = "Log in",
                    emailValue = fieldsState.emailValue,
                    pwdValue = fieldsState.pwdValue,
                    updateEmail = updateEmailListener,
                    updatePwd = updatePwdListener,
                    logUser =  { email, pwd -> logUserListener(email, pwd) },
                    keyboardController = keyboardController,
                    focusManager = focusManager
                )
                if (state.errorMessage.isNotBlank()) {
                    LaunchedEffect(System.currentTimeMillis()) {
                        snackbarHostState.showSnackbar(state.errorMessage)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    LoginScreen()
}