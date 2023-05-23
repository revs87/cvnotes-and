@file:OptIn(ExperimentalComposeUiApi::class)

package pt.android.instacv.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pt.android.instacv.R
import pt.android.instacv.theme.MyTheme
import pt.android.instacv.ui.component.LoadingIndicator
import pt.android.instacv.ui.util.Screen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen(
    navigateHomeListener: () -> Unit = {},
    state: AuthState,
    fieldsState: AuthFieldsState,
    registerClickListener: () -> Unit = {},
    loginClickListener: () -> Unit = {},
    updateEmailListener: (newValue: String) -> Unit = {},
    updatePwdListener: (newValue: String) -> Unit = {},
    createUserListener: (email: String, pwd: String) -> Unit = { _, _ -> },
    logUserListener: (email: String, pwd: String) -> Unit = { _, _ -> },
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.isLoggedIn) {
        LaunchedEffect(Unit) {
            navigateHomeListener.invoke()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LoadingIndicator(state.isLoading)
        when (state.section) {
            AuthSection.INTRO -> IntroSection(
                registerClickListener,
                loginClickListener
            )
            AuthSection.REGISTER -> {
                AuthSection(
                    title = "Create your account.",
                    emailTitle = "New email",
                    pwdTitle = "New password",
                    emailValue = fieldsState.emailValue,
                    pwdValue = fieldsState.pwdValue,
                    btnText = "Sign in",
                    updateEmail = updateEmailListener,
                    updatePwd = updatePwdListener,
                    createUser = { email, pwd -> createUserListener(email, pwd) },
                    keyboardController = keyboardController,
                    focusManager = focusManager)
            }
            AuthSection.LOGIN -> {
                AuthSection(
                    title = "Log into your account.",
                    emailTitle = "Email",
                    pwdTitle = "Password",
                    btnText = "Log in",
                    emailValue = fieldsState.emailValue,
                    pwdValue = fieldsState.pwdValue,
                    updateEmail = updateEmailListener,
                    updatePwd = updatePwdListener,
                    createUser =  { email, pwd -> logUserListener(email, pwd) },
                    keyboardController = keyboardController,
                    focusManager = focusManager)
            }
        }
        if (state.errorMessage.isNotBlank()) {
            LaunchedEffect(System.currentTimeMillis()) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}

@Composable
private fun IntroSection(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Do you already have an account?")
        Button(onClick = { onRegisterClick.invoke() }) { Text(text = "Create account".uppercase()) }
        Button(onClick = { onLoginClick.invoke() }) { Text(text = "Login".uppercase()) }
    }
}

@Composable
private fun AuthSection(
    title: String = "Title",
    emailTitle: String = "Email title",
    pwdTitle: String = "Pwd title",
    btnText: String = "Btn text",
    emailValue: String = "",
    pwdValue: String = "",
    updateEmail: (newValue: String) -> Unit = {},
    updatePwd: (newValue: String) -> Unit = {},
    createUser: ((email: String, pwd: String) -> Unit)? = null,
    logUser: ((email: String, pwd: String) -> Unit?)? = null,
    keyboardController: SoftwareKeyboardController? = null,
    focusManager: FocusManager? = null
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
        EmailField(emailTitle, emailValue) { updateEmail.invoke(it) }
        PasswordField(pwdTitle, pwdValue, { updatePwd.invoke(it) }) {
            keyboardController?.hide()
            focusManager?.clearFocus()
            createUser?.invoke(emailValue, pwdValue)
            logUser?.invoke(emailValue, pwdValue)
        }
        Button(onClick = {
            keyboardController?.hide()
            focusManager?.clearFocus()
            createUser?.invoke(emailValue, pwdValue)
            logUser?.invoke(emailValue, pwdValue)
        }) {
            Text(text = btnText.uppercase())
        }
    }
}

@Composable
private fun EmailField(
    title: String = "",
    value: String = "",
    onValueChange: (newValue: String) -> Unit = {}
) {
    TextField(
        modifier = Modifier.autofill(
            autofillTypes = listOf(AutofillType.EmailAddress),
            onFill = { onValueChange.invoke(it) },
        ),
        singleLine = true,
        label = { Text(text = title.uppercase()) },
        value = value,
        onValueChange = { onValueChange.invoke(it) },
    )
}

@Composable
private fun PasswordField(
    title: String = "",
    value: String = "",
    onValueChange: (newValue: String) -> Unit = {},
    onKeyboardAction: () -> Unit = {}
) {
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

    TextField(
        label = { Text(text = title.uppercase()) },
        value = value,
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = { onValueChange.invoke(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = { onKeyboardAction.invoke() }
        ),
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        modifier = Modifier.size(45.dp).padding(8.dp),
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off),
                            contentDescription = ""
                    )
            }
        },
    )
}

private fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this
        .onGloballyPositioned {
            autofillNode.boundingBox = it.boundsInWindow()
        }
        .onFocusChanged { focusState ->
            autofill?.run {
                if (focusState.isFocused) {
                    requestAutofillForNode(autofillNode)
                } else {
                    cancelAutofillForNode(autofillNode)
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme {
        PasswordField()
    }
}