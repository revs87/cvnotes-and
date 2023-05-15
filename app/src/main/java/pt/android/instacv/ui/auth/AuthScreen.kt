package pt.android.instacv.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.android.instacv.theme.MyTheme
import pt.android.instacv.ui.util.Screen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state: AuthState = viewModel.state.value
    val fieldsState: AuthFieldsState = viewModel.fieldsState.value
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.isLoggedIn) {
        navController.navigate(route = Screen.HomeScreen.route)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }
        when (state.section) {
            AuthSection.INTRO -> IntroSection(viewModel)
            AuthSection.REGISTER -> {
                RegisterSection(
                    fieldsState,
                    viewModel,
                    keyboardController,
                    focusManager)
            }
            AuthSection.LOGIN -> {
                LoginSection(
                    fieldsState,
                    viewModel,
                    keyboardController,
                    focusManager)
            }
        }
        if (state.errorMessage.isNotBlank()) {
            LaunchedEffect(System.currentTimeMillis()) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun IntroSection(
    viewModel: AuthViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Do you already have an account?")
        Button(onClick = { viewModel.onRegisterClick() }) {
            Text(text = "Create account".uppercase())
        }
        Button(onClick = { viewModel.onLoginClick() }) {
            Text(text = "Login".uppercase())
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RegisterSection(
    fieldsState: AuthFieldsState,
    viewModel: AuthViewModel,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create your account.")
        TextField(
            label = { Text(text = "New email".uppercase()) },
            value = fieldsState.emailValue,
            onValueChange = { viewModel.updateEmail(it) },
        )
        TextField(
            label = { Text(text = "New password".uppercase()) },
            value = fieldsState.pwdValue,
            onValueChange = { viewModel.updatePwd(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    viewModel.createUser(fieldsState.emailValue, fieldsState.pwdValue)
                }
            )
        )
        Button(onClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
            viewModel.createUser(fieldsState.emailValue, fieldsState.pwdValue)
        }) {
            Text(text = "Sign in".uppercase())
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LoginSection(
    fieldsState: AuthFieldsState,
    viewModel: AuthViewModel,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Log into your account.")
        TextField(
            label = { Text(text = "Email".uppercase()) },
            value = fieldsState.emailValue,
            onValueChange = { viewModel.updateEmail(it) },
        )
        TextField(
            label = { Text(text = "Password".uppercase()) },
            value = fieldsState.pwdValue,
            onValueChange = { viewModel.updatePwd(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    viewModel.logUser(fieldsState.emailValue, fieldsState.pwdValue)
                }
            )
        )
        Button(onClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
            viewModel.logUser(fieldsState.emailValue, fieldsState.pwdValue)
        }) {
            Text(text = "Log in".uppercase())
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme {
        val navController = rememberNavController()
        AuthScreen(navController)
    }
}