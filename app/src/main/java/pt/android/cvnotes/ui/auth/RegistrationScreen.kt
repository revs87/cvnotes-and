package pt.android.cvnotes.ui.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.component.AuthFields
import pt.android.cvnotes.ui.util.component.AuthFieldsState
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.auth.AuthError.*
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreen(
    state: AuthState = AuthState(),
    fieldsState: AuthFieldsState = AuthFieldsState(),
    errorMessage: String = "",
    updateEmailListener: (newValue: String) -> Unit = {},
    updatePwdListener: (newValue: String) -> Unit = {},
    createUserListener: (email: String, pwd: String) -> Unit = { _, _ -> },
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
                Box {
                    AuthFields(
                        title = "Create your account.",
                        emailTitle = "New email",
                        pwdTitle = "New password",
                        emailValue = fieldsState.emailValue,
                        pwdValue = fieldsState.pwdValue,
                        submitBtnEnabled = fieldsState.submitBtnEnabled,
                        btnText = "Sign in",
                        updateEmail = updateEmailListener,
                        updatePwd = updatePwdListener,
                        createUser = { email, pwd -> createUserListener(email, pwd) },
                        keyboardController = keyboardController,
                        focusManager = focusManager
                    )
                    LoadingIndicator(
                        modifier = Modifier
                            .size(LoadingIndicatorSize)
                            .align(Alignment.BottomEnd),
                        state.isLoading
                    )
                }
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
    RegistrationScreen()
}