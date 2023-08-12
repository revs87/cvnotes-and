package pt.android.cvnotes.ui.auth

import android.annotation.SuppressLint
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
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.component.AuthFields
import pt.android.cvnotes.ui.util.component.AuthFieldsState
import pt.android.cvnotes.ui.util.component.LoadingIndicator

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    state: AuthState = AuthState(),
    fieldsState: AuthFieldsState = AuthFieldsState(),
    errorMessage: String = "",
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
            ) {
                LoadingIndicator(state.isLoading)
                AuthFields(
                    title = "Log into your account.",
                    emailTitle = "Email",
                    pwdTitle = "Password",
                    btnText = "Log in",
                    emailValue = fieldsState.emailValue,
                    pwdValue = fieldsState.pwdValue,
                    submitBtnEnabled = fieldsState.submitBtnEnabled,
                    updateEmail = updateEmailListener,
                    updatePwd = updatePwdListener,
                    logUser =  { email, pwd -> logUserListener(email, pwd) },
                    keyboardController = keyboardController,
                    focusManager = focusManager
                )
                if (errorMessage.isNotBlank()) {
                    LaunchedEffect(true) {
                        snackbarHostState.showSnackbar(errorMessage)
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